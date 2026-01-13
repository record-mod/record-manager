package dev.tralwdwd.record.manager.installers

import android.os.Parcelable

interface InstallerError : Parcelable {
    val message: String
}
