package com.example.metgalleryproject.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.metgalleryproject.data.model.ArtObject

class FavouritesViewModel : ViewModel() {
    private val _favouriteArtObjects = mutableStateListOf<ArtObject>()
    val favouriteArtObjects: List<ArtObject> get() = _favouriteArtObjects

    fun addToFavorites(art: ArtObject) {
        if (!_favouriteArtObjects.contains(art)) {
            _favouriteArtObjects.add(art)
        }
    }

    fun removeFromFavorites(art: ArtObject) {
        _favouriteArtObjects.remove(art)
    }

    fun isFavorite(art: ArtObject): Boolean {
        return _favouriteArtObjects.contains(art)
    }
}