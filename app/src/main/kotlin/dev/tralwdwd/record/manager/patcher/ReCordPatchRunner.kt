package dev.tralwdwd.record.manager.patcher

import dev.tralwdwd.record.manager.patcher.steps.download.*
import dev.tralwdwd.record.manager.patcher.steps.install.*
import dev.tralwdwd.record.manager.patcher.steps.patch.*
import dev.tralwdwd.record.manager.patcher.steps.prepare.*
import dev.tralwdwd.record.manager.ui.screens.patchopts.PatchOptions
import kotlinx.collections.immutable.persistentListOf

class ReCordPatchRunner(options: PatchOptions) : StepRunner() {
    override val steps =
            persistentListOf(
                    FetchDiscordRNAStep(options),
                    DowngradeCheckStep(options),
                    RestoreDownloadsStep(),
                    DownloadDiscordRNABaseStep(),
                    DownloadDiscordRNALangStep(),
                    DownloadDiscordRNAResourcesStep(),
                    DownloadDiscordRNALibStep(),
                    DownloadReCordXposedStep(),
                    CopyDependenciesStep(),
                    PatchIconsStep(options),
                    PatchReCordManifestStep(options),
                    SaveMetadataStep(options),
                    AlignmentStep(),
                    SigningStep(),
                    InjectReCordXposedStep(),
                    InstallStep(options),
                    CleanupStep(),
            )
}
