package dev.tralwdwd.record.manager.patcher.steps.download

import dev.tralwdwd.record.manager.manager.PathManager
import dev.tralwdwd.record.manager.network.services.RNATrackerService.Companion.BASE_URL
import dev.tralwdwd.record.manager.patcher.steps.base.DownloadDiscordRNAStep
import dev.tralwdwd.record.manager.R
import org.koin.core.component.inject

class DownloadDiscordRNALangStep : DownloadDiscordRNAStep() {
    private val paths: PathManager by inject()

    override val targetUrl get() = "$BASE_URL/tracker/download/$version/config.en"
    override val targetFile get() = paths.discordApkVersionCache(version).resolve("config.en.apk")

    override val localizedName = R.string.patch_step_dl_langapk
}
