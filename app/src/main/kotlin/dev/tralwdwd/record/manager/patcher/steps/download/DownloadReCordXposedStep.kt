package dev.tralwdwd.record.manager.patcher.steps.download

import dev.tralwdwd.record.manager.manager.PathManager
import dev.tralwdwd.record.manager.network.services.ReCordGithubService
import dev.tralwdwd.record.manager.network.utils.SemVer
import dev.tralwdwd.record.manager.patcher.StepRunner
import dev.tralwdwd.record.manager.patcher.steps.base.DownloadStep
import dev.tralwdwd.record.manager.R
import dev.tralwdwd.record.manager.network.utils.getOrThrow
import org.koin.core.component.inject

class DownloadReCordXposedStep : DownloadStep() {
    private val paths: PathManager by inject()
    private val reCordGithubService: ReCordGithubService by inject()

    override val localizedName = R.string.patch_step_dl_record_xposed
    override val targetFile get() = paths.cachedReCordXposed(targetVersion)
    override lateinit var targetUrl: String

    lateinit var targetVersion: SemVer
        private set

    override suspend fun execute(container: StepRunner) {
        val latestRelease = reCordGithubService.getLatestXposedRelease().getOrThrow()
        container.log("Latest ReCordXposed release is ${latestRelease.name}")

        targetVersion = SemVer.parse(latestRelease.name)
        targetUrl = latestRelease.assets.first { it.name == "app-release.apk" }.browserDownloadUrl
        super.execute(container)
    }
}
