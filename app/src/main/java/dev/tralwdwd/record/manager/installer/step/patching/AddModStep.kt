package dev.tralwdwd.record.manager.installer.step.patching

import dev.tralwdwd.record.manager.BuildConfig
import dev.tralwdwd.record.manager.R
import dev.tralwdwd.record.manager.installer.step.Step
import dev.tralwdwd.record.manager.installer.step.StepGroup
import dev.tralwdwd.record.manager.installer.step.StepRunner
import dev.tralwdwd.record.manager.installer.step.download.DownloadModStep
import java.io.File

/**
 * Uses LSPatch to inject the ReCord XPosed module into Discord
 *
 * @param signedDir The signed apks to patch
 * @param lspatchedDir Output directory for LSPatch
 */
class AddModStep(
    private val signedDir: File,
    private val lspatchedDir: File
) : Step() {

    override val group = StepGroup.PATCHING
    override val nameRes = R.string.step_add_mod

    override suspend fun run(runner: StepRunner) {
        val mod = runner.getCompletedStep<DownloadModStep>().workingCopy

        runner.logger.i("Adding ${BuildConfig.MOD_NAME}Xposed module with LSPatch")
        val files = signedDir.listFiles()
            ?.takeIf { it.isNotEmpty() }
            ?: throw Error("Missing APKs from signing step")

        dev.tralwdwd.record.manager.installer.util.Patcher.patch(
            runner.logger,
            outputDir = lspatchedDir,
            apkPaths = files.map { it.absolutePath },
            embeddedModules = listOf(mod.absolutePath)
        )
    }

}