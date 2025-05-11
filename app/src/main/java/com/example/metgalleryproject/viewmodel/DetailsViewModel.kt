package com.example.metgalleryproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metgalleryproject.data.model.ArtObject
import com.example.metgalleryproject.repository.MetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(private val repository: MetRepository) : ViewModel() {

    private val _artDetails = MutableStateFlow<ArtObject?>(null)
    val artDetails: StateFlow<ArtObject?> get() = _artDetails

    fun fetchArtDetails(artId: Int) {
        viewModelScope.launch {
            val details = repository.getArtObjectDetails(artId)

            _artDetails.value = details
        }
    }

}
