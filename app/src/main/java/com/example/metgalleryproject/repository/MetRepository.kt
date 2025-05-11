package com.example.metgalleryproject.repository

import com.example.metgalleryproject.data.model.ArtObject
import com.example.metgalleryproject.data.network.MetMuseumApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MetRepository(private val api: MetMuseumApi) {

    private val maxResults = 10

    suspend fun searchArtObjects(query: String): List<ArtObject> {
        return withContext(Dispatchers.IO) {
            val response = api.searchArtObjects(query)
            val objectIds = response.objectIDs ?: emptyList()

            println("Search Response IDs: $objectIds")

            objectIds.take(maxResults).mapNotNull { id ->
                try {
                    val artObject = api.getArtObjectDetails(id)
                    println("Art Object: $artObject")
                    artObject
                } catch (e: Exception) {
                    println("Error fetching object $id: ${e.message}")
                    null
                }
            }
        }
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
