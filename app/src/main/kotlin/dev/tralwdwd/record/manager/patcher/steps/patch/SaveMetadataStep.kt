package dev.tralwdwd.record.manager.patcher.steps.patch

import dev.tralwdwd.record.manager.network.utils.SemVer
import dev.tralwdwd.record.manager.patcher.InstallMetadata
import dev.tralwdwd.record.manager.patcher.StepRunner
import dev.tralwdwd.record.manager.patcher.steps.StepGroup
import dev.tralwdwd.record.manager.patcher.steps.base.Step
import dev.tralwdwd.record.manager.patcher.steps.download.*
import dev.tralwdwd.record.manager.ui.screens.patchopts.PatchOptions
import com.github.diamondminer88.zip.ZipWriter
import dev.tralwdwd.record.manager.BuildConfig
import dev.tralwdwd.record.manager.R
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.lsposed.lspatch.share.LSPConfig

/**
 * Store the install options and additional data inside the APK for future use,
 * for example checking what library versions were used, or performing "updates" while
 * maintaining the same install options as what was used upon first install.
 */
class SaveMetadataStep(private val options: PatchOptions) : Step(), KoinComponent {
    private val json: Json by inject()

    override val group = StepGroup.Patch
    override val localizedName = R.string.patch_step_save_metadata

    override suspend fun execute(container: StepRunner) {
        val apk = container.getStep<CopyDependenciesStep>().patchedApk
        val recordXposed = container.getStepOrNull<DownloadReCordXposedStep>()

        val metadata = InstallMetadata(
            options = options,
            customManager = !BuildConfig.RELEASE,
            managerVersion = SemVer.parse(BuildConfig.VERSION_NAME),
            lspatchVersion = LSPConfig.instance.VERSION_CODE,
            recordXposedVersion = recordXposed?.targetVersion,
        )

        container.log("Writing serialized install metadata to APK")
        ZipWriter(apk, /* append = */ true).use {
            it.writeEntry("record.json", json.encodeToString<InstallMetadata>(metadata))
        }
    }
}
