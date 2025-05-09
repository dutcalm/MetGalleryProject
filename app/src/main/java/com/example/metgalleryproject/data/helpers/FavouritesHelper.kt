package com.example.metgalleryproject.data.helpers

import com.example.metgalleryproject.data.model.ArtObject

object FavouritesHelper {

    private val favouriteArtObjects = mutableListOf<ArtObject>()

    fun getFavoriteArtObjects(): List<ArtObject> {
        return favouriteArtObjects
    }

    fun addToFavorites(art: ArtObject) {
        if (!isFavorite(art)) {
            favouriteArtObjects.add(art)
        }
    }

    fun removeFromFavorites(art: ArtObject) {
        favouriteArtObjects.remove(art)
    }

    fun isFavorite(art: ArtObject): Boolean {
        return favouriteArtObjects.contains(art)
    }
}
