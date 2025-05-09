package com.example.metgalleryproject.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ArtObject(
    val id: Int,
    val title: String,
    val artist: String,
    val imageUrl: String
)