package com.example.metgalleryproject.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.metgalleryproject.ui.components.CustomLoadingIndicator
import com.example.metgalleryproject.ui.components.CustomSearchBar
import com.example.metgalleryproject.viewmodel.FavouritesViewModel
import com.example.metgalleryproject.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel,
    favouritesViewModel: FavouritesViewModel
) {
    val searchQuery = remember { mutableStateOf("") }
    val searchResults = viewModel.searchResults.collectAsLazyPagingItems()
    val hasSearchBeenTriggered = remember { mutableStateOf(false) }
    val hasStartedLoading = remember { mutableStateOf(false) }

    // Declanșăm căutarea
    fun onSearchClick() {
        if (searchQuery.value.isNotBlank()) {
            viewModel.search(searchQuery.value)
            hasSearchBeenTriggered.value = true
        }
    }

    // Observăm dacă a început efectiv încărcarea
    LaunchedEffect(searchResults.loadState.refresh) {
        if (hasSearchBeenTriggered.value && searchResults.loadState.refresh is LoadState.Loading) {
            hasStartedLoading.value = true
        }
    }

    val showInitialLoading by remember {
        derivedStateOf {
            hasStartedLoading.value &&
                    searchResults.loadState.refresh is LoadState.Loading &&
                    searchResults.itemCount == 0
        }
    }

    val showNoResults by remember {
        derivedStateOf {
            hasStartedLoading.value &&
                    searchResults.loadState.refresh is LoadState.NotLoading &&
                    searchResults.itemCount == 0
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        CustomSearchBar(
            query = searchQuery.value,
            onQueryChange = { searchQuery.value = it },
            onSearchClick = { onSearchClick() }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                showInitialLoading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillParentMaxSize()
                                .padding(top = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CustomLoadingIndicator()
                        }
                    }
                }

                showNoResults -> {
                    item {
                        Text(
                            "No results found",
                            color = Color.Gray,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                searchResults.loadState.refresh is LoadState.Error -> {
                    val error = (searchResults.loadState.refresh as LoadState.Error).error
                    item {
                        ErrorItem("Loading error: ${error.localizedMessage}")
                    }
                }

                else -> {
                    items(
                        count = searchResults.itemCount,
                        key = { index -> searchResults.peek(index)?.id ?: index }
                    ) { index ->
                        val art = searchResults[index]
                        if (art != null) {
                            ArtItem(
                                art = art,
                                navController = navController,
                                viewModel = favouritesViewModel
                            )
                            HorizontalDivider(
                                color = Color.LightGray,
                                thickness = 1.dp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // append (next page) loading/error
            when (val appendState = searchResults.loadState.append) {
                is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CustomLoadingIndicator()
                        }
                    }
                }

                is LoadState.Error -> {
                    item {
                        ErrorItem("Loading error: ${appendState.error.localizedMessage}")
                    }
                }

                else -> {}
            }
        }
    }
}
