/*
 * Copyright (c) 2022 Juby210 & zt
 * Licensed under the Open Software License version 3.0
 */

package dev.tralwdwd.record.manager.network.models

import dev.tralwdwd.record.manager.network.utils.SemVer
import dev.tralwdwd.record.manager.util.serialization.IntAsStringSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BuildInfo(
    @Serializable(with = IntAsStringSerializer::class)
    @SerialName("versionCode")
    val discordVersionCode: Int,

    // @SerialName("versionName")
    // val discordVersionName: Int,

    @SerialName("injectorVersion")
    val injectorVersion: SemVer,

    @SerialName("patchesVersion")
    val patchesVersion: SemVer,
)
