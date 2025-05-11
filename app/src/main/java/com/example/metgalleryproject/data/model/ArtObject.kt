package com.example.metgalleryproject.data.model

import com.squareup.moshi.Json

data class ArtObject(
    @Json(name = "objectID")
    val id: Int,

    @Json(name = "title")
    val title: String,

    @Json(name = "artistDisplayName")
    val artist: String,

    @Json(name = "primaryImageSmall")
    val imageUrl: String?,

    @Json(name = "objectDate")
    val year: String?,

    @Json(name = "medium")
    val medium: String?,

    @Json(name = "dimensions")
    val dimensions: String?,

    @Json(name = "department")
    val department: String?,

    @Json(name = "country")
    val country: String?,

    @Json(name = "additionalImages")
    val additionalImages: List<String>
)
