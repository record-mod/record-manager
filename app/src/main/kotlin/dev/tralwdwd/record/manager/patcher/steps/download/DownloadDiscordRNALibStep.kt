package dev.tralwdwd.record.manager.patcher.steps.download

import android.os.Build
import dev.tralwdwd.record.manager.manager.PathManager
import dev.tralwdwd.record.manager.network.services.RNATrackerService.Companion.BASE_URL
import dev.tralwdwd.record.manager.patcher.steps.base.DownloadDiscordRNAStep
import dev.tralwdwd.record.manager.R
import org.koin.core.component.inject

class DownloadDiscordRNALibStep : DownloadDiscordRNAStep() {
    private val paths: PathManager by inject()
    private val arch = Build.SUPPORTED_ABIS.first().replace("-v", "_v")

    override val targetUrl get() = "$BASE_URL/tracker/download/$version/config.$arch"
    override val targetFile get() = paths.discordApkVersionCache(version).resolve("config.$arch.apk")
    override val localizedName = R.string.patch_step_dl_libapk
}
