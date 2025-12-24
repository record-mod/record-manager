package dev.tralwdwd.record.manager.network.utils

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.tralwdwd.record.manager.BuildConfig
import dev.tralwdwd.record.manager.domain.repository.RestRepository
import dev.tralwdwd.record.manager.network.dto.Commit

class CommitsPagingSource(
    private val repo: RestRepository
) : PagingSource<Int, Commit>() {

    override fun getRefreshKey(state: PagingState<Int, Commit>): Int? =
        state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Commit> {
        val page = params.key ?: 1

        return when (val response = repo.getCommits(BuildConfig.REPO, page)) {
            is ApiResponse.Success -> LoadResult.Page(
                data = response.data,
                prevKey = if (page > 1) page - 1 else null,
                nextKey = if (response.data.isNotEmpty()) page + 1 else null
            )

            is ApiResponse.Failure -> LoadResult.Error(response.error)
            is ApiResponse.Error -> LoadResult.Error(response.error)
        }
    }

}