package dev.tralwdwd.record.manager.patcher.steps.prepare

import dev.tralwdwd.record.manager.network.services.RNATrackerService
import dev.tralwdwd.record.manager.patcher.StepRunner
import dev.tralwdwd.record.manager.patcher.steps.StepGroup
import dev.tralwdwd.record.manager.patcher.steps.base.Step
import dev.tralwdwd.record.manager.ui.screens.patchopts.PatchOptions
import dev.tralwdwd.record.manager.ui.screens.patchopts.VersionPreference
import dev.tralwdwd.record.manager.R
import dev.tralwdwd.record.manager.network.utils.getOrThrow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.properties.Delegates

class FetchDiscordRNAStep(val options: PatchOptions) : Step(), KoinComponent {
    private val rnaTrackerService: RNATrackerService by inject()

    override val group: StepGroup = StepGroup.Prepare
    override val localizedName: Int = R.string.patch_step_fetch_rna

    var targetVersion by Delegates.notNull<Int>()
        private set

    override suspend fun execute(container: StepRunner) {
        if (options.versionPreference == VersionPreference.Custom) {
            container.log("Custom version selected, skipping fetch.")
            targetVersion = options.customVersionCode.toInt()
            return
        }

        val latestVal = rnaTrackerService.getLatestDiscordVersions().getOrThrow()
        container.log("Fetched latest discord info: $latestVal")

        targetVersion = when (options.versionPreference) {
            VersionPreference.Stable -> latestVal.latest.stable
            VersionPreference.Beta -> latestVal.latest.beta
            VersionPreference.Alpha -> latestVal.latest.alpha
            else -> throw IllegalArgumentException("Unsupported version preference: ${options.versionPreference}")
        }

        container.log("Selected Discord version: $targetVersion")
    }
}
