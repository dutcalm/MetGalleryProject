package com.example.metgalleryproject.data.network

import com.example.metgalleryproject.data.model.ArtObject
import com.example.metgalleryproject.data.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MetMuseumApi {
    @GET("search")
    suspend fun searchArtObjects(
        @Query("q") query: String,
        @Query("hasImages") hasImages: Boolean = true
    ): SearchResponse

    @GET("objects/{objectID}")
    suspend fun getArtObjectDetails(
        @Path("objectID") objectId: Int
    ): ArtObject
}