package dev.tralwdwd.record.manager.ui.screens.home

import android.app.Application
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.provider.Settings
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.tralwdwd.record.manager.network.models.BuildInfo
import dev.tralwdwd.record.manager.network.models.RNATrackerIndex
import dev.tralwdwd.record.manager.network.services.*
import dev.tralwdwd.record.manager.network.utils.SemVer
import dev.tralwdwd.record.manager.patcher.InstallMetadata
import dev.tralwdwd.record.manager.ui.screens.patchopts.*
import dev.tralwdwd.record.manager.ui.util.DiscordVersion
import dev.tralwdwd.record.manager.ui.util.toUnsafeImmutable
import dev.tralwdwd.record.manager.util.*
import com.github.diamondminer88.zip.ZipReader
import dev.tralwdwd.record.manager.BuildConfig
import dev.tralwdwd.record.manager.R
import dev.tralwdwd.record.manager.network.utils.fold
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream

class HomeModel(
    private val application: Application,
    private val maven: AliucordMavenService,
    private val recordGithub: ReCordGithubService,
    private val rnaTracker: RNATrackerService,
    private val json: kotlinx.serialization.json.Json,
    private val preferences: dev.tralwdwd.record.manager.manager.PreferencesManager,
) : ScreenModel {
    var installsState by mutableStateOf<InstallsState>(InstallsState.Fetching)
        private set

    var showRootDetectedDialog by mutableStateOf(false)
    var rootDetected by mutableStateOf(false)

    private val refreshingLock = Mutex()
    private var remoteDataJson: BuildInfo? = null
    private var trackerIndexJson: RNATrackerIndex? = null
    private var latestReCordXposedVersion: SemVer? = null

    init {
        refresh()
        checkAndSetRootInstaller()
    }

    fun refresh(delay: Boolean = false) = screenModelScope.launchIO {
        if (refreshingLock.isLocked) return@launchIO

        if (delay) {
            delay(250)

            if (refreshingLock.isLocked)
                return@launchIO
        }

        refreshingLock.withLock {
            val packages = fetchAliucordPackages()

            val jobs = listOf(
                screenModelScope.launch(Dispatchers.IO) {
                    fetchInstallations(packages)
                },
                screenModelScope.launch(Dispatchers.IO) {
                    if (remoteDataJson == null)
                        fetchRemoteData()
                }
            )

            jobs.joinAll()
            mainThread { refreshInstallationsUpToDate(packages) }
        }
    }

    fun uninstallApp(packageName: String) {
        val uninstallIntent = Intent(Intent.ACTION_DELETE).apply {
            data = "package:$packageName".toUri()
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        application.startActivity(uninstallIntent)
    }

    fun openApp(packageName: String) {
        val launchIntent = application.packageManager
            .getLaunchIntentForPackage(packageName)

        if (launchIntent != null) {
            application.startActivity(launchIntent)
        } else {
            application.showToast(R.string.launch_aliucord_fail)
        }
    }

    fun openAppInfo(packageName: String) {
        val launchIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .setData("package:$packageName".toUri())

        application.startActivity(launchIntent)
    }

    /**
     * Creates a [PatchOptionsScreen] that can be navigated to,
     * with prefilled options from an existing installation.
     */
    fun createPrefilledPatchOptsScreen(packageName: String): PatchOptionsScreen {
        val metadata = try {
            val applicationInfo = application.packageManager.getApplicationInfo(packageName, 0)
            val metadataFile = ZipReader(applicationInfo.publicSourceDir)
                .use { it.openEntry("record.json")?.read() }

            @OptIn(ExperimentalSerializationApi::class)
            metadataFile?.let { json.decodeFromStream<InstallMetadata>(it.inputStream()) }
        } catch (t: Throwable) {
            Log.w(BuildConfig.TAG, "Failed to parse ReCord install metadata from package $packageName", t)
            null
        }

        val patchOptions = metadata?.options
            ?: PatchOptions.Default.copy(packageName = packageName)

        return PatchOptionsScreen(prefilledOptions = patchOptions)
    }

    private suspend fun fetchInstallations(packages: List<PackageInfo>) {
        mainThread {
            if (installsState !is InstallsState.Fetched)
                installsState = InstallsState.Fetching
        }

        try {
            val packageManager = application.packageManager
            val aliucordInstallations = packages.mapNotNull { pkg ->
                // `longVersionCode` is unnecessary since Discord doesn't use `versionCodeMajor`
                @Suppress("DEPRECATION")
                val versionCode = pkg.versionCode
                val versionName = pkg.versionName ?: return@mapNotNull null
                val applicationInfo = pkg.applicationInfo ?: return@mapNotNull null

                InstallData(
                    name = packageManager.getApplicationLabel(applicationInfo).toString(),
                    packageName = pkg.packageName,
                    isUpToDate = isInstallationUpToDate(pkg),
                    icon = packageManager
                        .getApplicationIcon(applicationInfo)
                        .toBitmap()
                        .asImageBitmap()
                        .let(::BitmapPainter),
                    version = DiscordVersion.Existing(
                        type = DiscordVersion.parseVersionType(versionCode),
                        name = versionName.split("-")[0].trim(),
                        code = versionCode,
                    ),
                )
            }

            mainThread {
                installsState = if (aliucordInstallations.isNotEmpty()) {
                    InstallsState.Fetched(data = aliucordInstallations.toUnsafeImmutable())
                } else {
                    InstallsState.None
                }
            }
        } catch (t: Throwable) {
            Log.e(BuildConfig.TAG, "Failed to query Aliucord installations", t)
            mainThread { installsState = InstallsState.Error }
        }
    }

    private suspend fun refreshInstallationsUpToDate(packages: List<PackageInfo>) {
        val installations = mainThread { (installsState as? InstallsState.Fetched)?.data }
            ?: return

        try {
            val newInstallations = installations.map { data ->
                val packageInfo = packages.find { it.packageName == data.packageName }
                    ?: throw IllegalStateException("Checking up-to-date status for package that has not been fetched")

                data.copy(isUpToDate = isInstallationUpToDate(packageInfo))
            }

            mainThread { installsState = InstallsState.Fetched(data = newInstallations.toUnsafeImmutable()) }
        } catch (t: Throwable) {
            Log.e(BuildConfig.TAG, "Failed to check installations up-to-date", t)
            mainThread { installsState = InstallsState.Error }
        }
    }

    private suspend fun fetchRemoteData() {
        listOf(
            screenModelScope.launch(Dispatchers.IO) {
                rnaTracker.getLatestDiscordVersions().fold(
                    success = { trackerIndexJson = it },
                    fail = { Log.w(BuildConfig.TAG, "Failed to fetch latest Discord RNA versions", it) },
                )
            },
            screenModelScope.launch(Dispatchers.IO) {
                recordGithub.getLatestXposedRelease().fold(
                    success = {
                        val versionName = it.name
                        val truncatedVersion = versionName.split(".").take(3).joinToString(".")
                        latestReCordXposedVersion = SemVer.parse(truncatedVersion)
                    },
                    fail = { Log.w(BuildConfig.TAG, "Failed to fetch latest ReCordXposed version", it) },
                )
            },

            ).joinAll()

        if (trackerIndexJson == null /* remoteDataJson == null || latestAliuhookVersion == null */ || latestReCordXposedVersion == null) {
            mainThread { application.showToast(R.string.home_network_fail) }
        }
    }

    private fun isRootAvailable(): Boolean {
        return try {
            val process = ProcessBuilder("su", "-c", "echo rooted").start()
            val output = process.inputStream.bufferedReader().readText().trim()
            process.waitFor()
            output == "rooted"
        } catch (e: Exception) {
            false
        }
    }

    private fun checkAndSetRootInstaller() {
        if (isRootAvailable() && !rootDetected) {
            rootDetected = true
            // Only show dialog and auto-switch if the popup hasn't been shown before
            if (!preferences.rootPopupShown) {
                showRootDetectedDialog = true
                preferences.rootPopupShown = true
                // Switch installer to ROOT only the first time
                if (preferences.installer != dev.tralwdwd.record.manager.manager.InstallerSetting.ROOT) {
                    preferences.installer = dev.tralwdwd.record.manager.manager.InstallerSetting.ROOT
                }
            }

        }
    }

    /**
     * Obtains all installed packages on the device that are an Aliucord installation.
     */
    private fun fetchAliucordPackages(): List<PackageInfo> {
        return application.packageManager
            .getInstalledPackages(PackageManager.GET_META_DATA)
            .filter {
                return@filter it.applicationInfo?.metaData?.containsKey("isReCord") == true
            }
    }

    /**
     * Checks whether the current ReCord installation is up-to-date.
     *
     * Currently mirrors the behavior of Bunny Manager by directly comparing against
     * the latest available Discord and ReCordXposed release. This is a temporary approach and will remain
     * in place until ReCord adds support for version pinning.
     */
    private fun isInstallationUpToDate(pkg: PackageInfo): Boolean? {
        val trackerData = trackerIndexJson ?: return null

        // `longVersionCode` is unnecessary since Discord doesn't use `versionCodeMajor`
        @Suppress("DEPRECATION")
        val versionCode = pkg.versionCode

        // Try to parse install metadata. If none present, install was made via legacy installer.
        val apkPath = pkg.applicationInfo?.publicSourceDir ?: return false
        val installMetadata = try {
            val metadataFile = ZipReader(apkPath).use { it.openEntry("record.json")?.read() }
                ?: return false

            @OptIn(ExperimentalSerializationApi::class)
            json.decodeFromStream<InstallMetadata>(metadataFile.inputStream())
        } catch (t: Throwable) {
            Log.w(BuildConfig.TAG, "Failed to parse ReCord install metadata from package ${pkg.packageName}", t)
            return false
        }

        val latestByPref = when (installMetadata.options.versionPreference) {
            VersionPreference.Stable -> trackerData.latest.stable
            VersionPreference.Beta -> trackerData.latest.beta
            VersionPreference.Alpha -> trackerData.latest.alpha
            VersionPreference.Custom -> return true
        }

        return latestByPref == versionCode && installMetadata.recordXposedVersion == latestReCordXposedVersion
    }
}
