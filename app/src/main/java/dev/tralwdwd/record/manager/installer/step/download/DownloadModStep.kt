package dev.tralwdwd.record.manager.installer.step.download

import androidx.compose.runtime.Stable
import dev.tralwdwd.record.manager.R
import dev.tralwdwd.record.manager.installer.step.download.base.DownloadStep
import java.io.File

/**
 * Downloads the ReCord XPosed module
 *
 * https://github.com/record-mod/record-xposed
 */
@Stable
class DownloadModStep(
    workingDir: File
): DownloadStep() {

    override val nameRes = R.string.step_dl_mod

    override val downloadFullUrl: String = preferenceManager.moduleUrl
    override val destination = preferenceManager.DEFAULT_MODULE_LOCATION
    override val workingCopy = workingDir.resolve("xposed.apk")

}
