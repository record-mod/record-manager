package dev.tralwdwd.record.manager.ui.viewmodel.settings

import android.content.Context
import android.os.Environment
import cafe.adriel.voyager.core.model.ScreenModel
import dev.tralwdwd.record.manager.BuildConfig
import java.io.File

class DeveloperSettingsViewModel(
    private val context: Context
): ScreenModel {
    private val cacheDir = context.externalCacheDir ?: File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOWNLOADS).resolve(
        BuildConfig.MANAGER_NAME).also { it.mkdirs() }

    fun clearCache() {
        cacheDir.deleteRecursively()
    }
}