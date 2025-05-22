package com.example.metgalleryproject.data.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.metgalleryproject.data.model.ArtObject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class SearchPagingSource(
    private val api: MetMuseumApi,
    private val query: String
) : PagingSource<Int, ArtObject>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArtObject> {
        val page = params.key ?: 1
        val pageSize = params.loadSize

        return try {
            val response = api.searchArtObjects(query)
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
                    .filter {
                        it.title.contains(query, ignoreCase = true) || it.artist.contains(
                            query,
                            ignoreCase = true
                        )
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
    override fun getRefreshKey(state: PagingState<Int, ArtObject>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }
}

