package com.example.metgalleryproject.data.model

import com.squareup.moshi.Json

data class SearchResponse(
    @Json(name = "objectIDs")
    val objectIDs: List<Int>?
)

