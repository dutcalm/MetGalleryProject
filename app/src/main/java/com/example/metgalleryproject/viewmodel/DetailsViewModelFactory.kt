package com.example.metgalleryproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.metgalleryproject.repository.MetRepository

class DetailsViewModelFactory(private val repository: MetRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass == DetailsViewModel::class.java) {
            @Suppress("UNCHECKED_CAST")
            return DetailsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
