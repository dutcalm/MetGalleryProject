package com.example.metgalleryproject.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.metgalleryproject.data.model.ArtObject
import com.example.metgalleryproject.data.network.MetMuseumApi
import com.example.metgalleryproject.data.network.SearchPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MetRepository(private val api: MetMuseumApi) {

    fun searchArtObjects(query: String): Flow<PagingData<ArtObject>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { SearchPagingSource(api, query) }
        ).flow
    }


    suspend fun getArtObjectDetails(objectId: Int): ArtObject? {
        return withContext(Dispatchers.IO) {
            try {
                api.getArtObjectDetails(objectId)
            } catch (e: Exception) {
                null
            }
        }
    }
}