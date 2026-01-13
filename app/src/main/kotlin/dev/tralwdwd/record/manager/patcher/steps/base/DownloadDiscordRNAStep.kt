package dev.tralwdwd.record.manager.patcher.steps.base

import dev.tralwdwd.record.manager.patcher.StepRunner
import dev.tralwdwd.record.manager.patcher.steps.prepare.FetchDiscordRNAStep
import kotlin.properties.Delegates

abstract class DownloadDiscordRNAStep : DownloadStep() {
    protected var version by Delegates.notNull<Int>()
        private set

    override suspend fun execute(container: StepRunner) {
        version = container.getStep<FetchDiscordRNAStep>().targetVersion
        super.execute(container)
    }
}
