package com.example.metgalleryproject.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var isSearching by remember { mutableStateOf(false) }
    var hasLoadedResults by remember { mutableStateOf(false) }

    LaunchedEffect(searchResults.loadState.refresh) {
        if (searchResults.loadState.refresh is LoadState.Loading) {
            hasLoadedResults = false
        } else if (searchResults.loadState.refresh is LoadState.NotLoading) {
            hasLoadedResults = true
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        CustomSearchBar(
            query = searchQuery.value,
            onQueryChange = { searchQuery.value = it },
            onSearchClick = {
                if (searchQuery.value.isNotBlank()) {
                    viewModel.search(searchQuery.value)
                    isSearching = true
                    hasLoadedResults = false
                }
            }
        )

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                isSearching && !hasLoadedResults -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CustomLoadingIndicator()
                    }
                }

                isSearching && hasLoadedResults && searchResults.itemCount == 0 -> {
                    Text(
                        text = "No results found",
                        color = Color.Gray,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }

                searchResults.loadState.refresh is LoadState.Error -> {
                    val error = (searchResults.loadState.refresh as LoadState.Error).error
                    ErrorItem(
                        message = "Loading error: ${error.localizedMessage}"
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
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

                        when (val appendState = searchResults.loadState.append) {
                            is LoadState.Loading -> {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CustomLoadingIndicator()
                                    }
                                }
                            }

                            is LoadState.Error -> {
                                item {
                                    ErrorItem(
                                        message = "Loading error: ${appendState.error.localizedMessage}"
                                    )
                                }
                            }

                            else -> Unit
                        }
                    }
                }
            }
        }
    }
}

