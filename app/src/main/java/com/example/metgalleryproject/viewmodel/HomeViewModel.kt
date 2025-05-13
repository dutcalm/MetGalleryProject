package com.example.metgalleryproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.metgalleryproject.data.model.ArtObject
import com.example.metgalleryproject.data.network.ArtObjectsPagingSource
import com.example.metgalleryproject.data.network.MetMuseumApiService
import kotlinx.coroutines.flow.Flow

class HomeViewModel : ViewModel() {

    val artObjectsFlow: Flow<PagingData<ArtObject>> = Pager(
        config = PagingConfig(pageSize = 20)
    ) {
        ArtObjectsPagingSource(MetMuseumApiService.api)
    }.flow.cachedIn(viewModelScope)

}