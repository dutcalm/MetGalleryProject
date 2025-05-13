package com.example.metgalleryproject.data.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.metgalleryproject.data.model.ArtObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class ArtObjectsPagingSource(
    private val api: MetMuseumApi,
) : PagingSource<Int, ArtObject>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArtObject> {
        val page = params.key ?: 1
        val pageSize = params.loadSize

        return try {
            val response = withContext(Dispatchers.IO) {
                api.searchArtObjects("art")
            }
            val ids = response.objectIDs ?: emptyList()

            val startIndex = (page - 1) * pageSize
            val endIndex = minOf(startIndex + pageSize, ids.size)

            if (startIndex >= ids.size) {
                return LoadResult.Page(
                    data = emptyList(),
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = null
                )
            }

            val pageIds = ids.subList(startIndex, endIndex)

//            val artObjects = pageIds.mapNotNull { id ->
//                fetchArtObjectWithRetry(id)
//            }

            val artObjects = coroutineScope {
                pageIds.map { id ->
                    async {
                        try {
                            api.getArtObjectDetails(id)
                        } catch (e: Exception) {
                            null
                        }
                    }
                }.awaitAll().filterNotNull()
                    .filter { artObject ->
                        !artObject.imageUrl.isNullOrEmpty()
                    }

            }

            LoadResult.Page(
                data = artObjects,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (endIndex >= ids.size) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private suspend fun fetchArtObjectWithRetry(id: Int, maxRetries: Int = 3): ArtObject? {
        var currentAttempt = 0
        while (currentAttempt < maxRetries) {
            try {
                return withContext(Dispatchers.IO) {
                    api.getArtObjectDetails(id)
                }
            } catch (e: Exception) {
                currentAttempt++
                if (currentAttempt >= maxRetries) {
                    return null
                }
            }
        }
        return null
    }

    override fun getRefreshKey(state: PagingState<Int, ArtObject>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }
}
