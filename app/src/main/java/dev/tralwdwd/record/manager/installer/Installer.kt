package dev.tralwdwd.record.manager.installer

import java.io.File

interface Installer {
    suspend fun installApks(silent: Boolean = false, vararg apks: File)
}