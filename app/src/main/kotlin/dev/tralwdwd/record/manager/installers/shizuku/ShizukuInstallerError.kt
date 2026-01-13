package dev.tralwdwd.record.manager.installers.shizuku

import android.content.Context
import android.os.Parcelable
import dev.tralwdwd.record.manager.installers.InstallerResult
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShizukuInstallerError(
    private val reason: String
) : InstallerResult.Error(), Parcelable {
    override fun getDebugReason(): String = reason
    override fun getLocalizedReason(context: Context): String? = null
}
