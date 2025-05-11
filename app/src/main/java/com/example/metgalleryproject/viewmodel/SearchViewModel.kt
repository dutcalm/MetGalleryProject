package com.example.metgalleryproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metgalleryproject.data.model.ArtObject
import com.example.metgalleryproject.repository.MetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: MetRepository) : ViewModel() {

    private val _artObjects = MutableStateFlow<List<ArtObject>>(emptyList())
    val artObjects: StateFlow<List<ArtObject>> get() = _artObjects

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun searchArtworks(query: String) {
        viewModelScope.launch {
            if (_isLoading.value) return@launch
            _isLoading.value = true

            try {
                val response = repository.searchArtObjects(query)
                val artObjects = response.mapNotNull { repository.getArtObjectDetails(it.id) }
                _artObjects.value = artObjects
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
