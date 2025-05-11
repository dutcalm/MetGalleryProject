package com.example.metgalleryproject.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metgalleryproject.data.model.ArtObject
import com.example.metgalleryproject.data.model.SearchResponse
import com.example.metgalleryproject.data.network.MetMuseumApiService
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _artObjects = mutableStateOf<List<ArtObject>>(emptyList())
    val artObjects: List<ArtObject> get() = _artObjects.value

    fun fetchArtObjects(query: String) {
        viewModelScope.launch {
            try {
                val searchResponse: SearchResponse = MetMuseumApiService.api.searchArtObjects(query)

                val artDetails = searchResponse.objectIDs?.mapNotNull { objectID ->
                    try {
                        MetMuseumApiService.api.getArtObjectDetails(objectID)
                    } catch (e: Exception) {
                        null
                    }
                } ?: emptyList()

                _artObjects.value = artDetails
            } catch (e: Exception) {
                _artObjects.value = emptyList()
            }
        }
    }
}