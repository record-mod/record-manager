package dev.tralwdwd.record.manager.installers.shizuku

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import dev.tralwdwd.record.manager.installers.InstallerResult

class ShizukuResultReceiver(
    private val sessionId: Int,
    private val isUninstall: Boolean,
    private val onResult: (InstallerResult) -> Unit
) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val extras = intent.extras ?: return
        val receivedSessionId = extras.getInt(ShizukuIntentReceiver.EXTRA_SESSION_ID)

        if (receivedSessionId == sessionId) {
            val result = extras.getParcelable<InstallerResult>(ShizukuIntentReceiver.EXTRA_RESULT)
            if (result != null) {
                onResult(result)
            }
        }
    }

    companion object {
        val intentFilter = IntentFilter("com.aliucord.manager.INSTALL_RESULT")
    }
}
