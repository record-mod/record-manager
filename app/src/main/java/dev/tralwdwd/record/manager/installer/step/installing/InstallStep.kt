package dev.tralwdwd.record.manager.installer.step.installing

import android.content.Context
import dev.tralwdwd.record.manager.R
import dev.tralwdwd.record.manager.domain.manager.InstallMethod
import dev.tralwdwd.record.manager.domain.manager.PreferenceManager
import dev.tralwdwd.record.manager.installer.Installer
import dev.tralwdwd.record.manager.installer.session.SessionInstaller
import dev.tralwdwd.record.manager.installer.shizuku.ShizukuInstaller
import dev.tralwdwd.record.manager.installer.shizuku.ShizukuPermissions
import dev.tralwdwd.record.manager.installer.step.Step
import dev.tralwdwd.record.manager.installer.step.StepGroup
import dev.tralwdwd.record.manager.installer.step.StepRunner
import dev.tralwdwd.record.manager.utils.isMiui
import dev.tralwdwd.record.manager.utils.showToast
import org.koin.core.component.inject
import java.io.File

/**
 * Installs all the modified splits with the users desired [Installer]
 *
 * @see SessionInstaller
 * @see ShizukuInstaller
 *
 * @param lspatchedDir Where all the patched APKs are
 */
class InstallStep(
    private val lspatchedDir: File
): Step() {

    private val preferences: PreferenceManager by inject()
    private val context: Context by inject()

    override val group = StepGroup.INSTALLING
    override val nameRes = R.string.step_installing

    override suspend fun run(runner: StepRunner) {
        runner.logger.i("Installing apks")
        val files = lspatchedDir.listFiles()
            ?.takeIf { it.isNotEmpty() }
            ?: throw Error("Missing APKs from LSPatch step; failure likely")

        val installMethod = if (preferences.installMethod == InstallMethod.SHIZUKU && !ShizukuPermissions.waitShizukuPermissions()) {
            // Temporarily use DEFAULT if SHIZUKU permissions are not granted
            context.showToast(R.string.msg_shizuku_denied)
            InstallMethod.DEFAULT
        } else {
            preferences.installMethod
        }

        val installer: Installer = when (installMethod) {
            InstallMethod.DEFAULT -> SessionInstaller(context)
            InstallMethod.SHIZUKU -> ShizukuInstaller(context)
        }

        installer.installApks(silent = !isMiui, *files)
    }

}