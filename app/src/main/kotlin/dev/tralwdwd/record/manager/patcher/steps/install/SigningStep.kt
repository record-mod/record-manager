package dev.tralwdwd.record.manager.patcher.steps.install

import dev.tralwdwd.record.manager.patcher.StepRunner
import dev.tralwdwd.record.manager.patcher.steps.StepGroup
import dev.tralwdwd.record.manager.patcher.steps.base.Step
import dev.tralwdwd.record.manager.patcher.steps.download.CopyDependenciesStep
import dev.tralwdwd.record.manager.patcher.util.Signer
import dev.tralwdwd.record.manager.R
import org.koin.core.component.KoinComponent

/**
 * Sign the APK with a keystore generated on-device.
 */
class SigningStep : Step(), KoinComponent {
    override val group = StepGroup.Install
    override val localizedName = R.string.patch_step_signing

    override suspend fun execute(container: StepRunner) {
        for (apk in container.getStep<CopyDependenciesStep>().patchedApks) {
            container.log("Signing apk at ${apk.absolutePath}")
            Signer.signApk(apk)
        }
    }
}
