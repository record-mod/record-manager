package dev.tralwdwd.record.manager.network.services

import dev.tralwdwd.record.manager.network.models.GithubRelease
import dev.tralwdwd.record.manager.network.utils.ApiResponse
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.http.HttpHeaders

class ReCordGithubService(
        private val http: HttpService,
) {
    /** Fetches all the Manager releases with a 60s local cache. */
    suspend fun getManagerReleases(): ApiResponse<List<GithubRelease>> {
        return http.request {
            url("https://api.github.com/repos/$ORG/$MANAGER_REPO/releases")
            header(HttpHeaders.CacheControl, "public, max-age=60, s-maxage=60")
        }
    }

    /** Fetches the latest Xposed release with a 60s local cache. */
    suspend fun getLatestXposedRelease(): ApiResponse<GithubRelease> {
        return http.request {
            url("https://api.github.com/repos/$ORG/$XPOSED_REPO/releases/latest")
            header(HttpHeaders.CacheControl, "public, max-age=60, s-maxage=60")
        }
    }

    companion object {
        const val ORG = "record-mod"
        const val MANAGER_REPO = "record-manager"
        const val XPOSED_REPO = "record-xposed"
    }
}
