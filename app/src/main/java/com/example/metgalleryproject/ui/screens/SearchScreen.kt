package com.example.metgalleryproject.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    val hasSearched = remember { mutableStateOf(false) }

    fun onSearchClick() {
        hasSearched.value = true
        viewModel.search(searchQuery.value)
        searchResults.refresh()
    }

    val showNoResults by remember {
        derivedStateOf {
            hasSearched.value &&
                    searchResults.loadState.refresh is LoadState.NotLoading &&
                    searchResults.itemCount == 0
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        CustomSearchBar(
            modifier = Modifier.fillMaxWidth(),
            query = searchQuery.value,
            onQueryChange = { searchQuery.value = it },
            onSearch = { onSearchClick() },
            searchResults = searchResults.itemSnapshotList.items,
            navController = navController,
            favouritesViewModel = favouritesViewModel,
            placeholder = { Text("Search for art") }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val loadState = searchResults.loadState.refresh

            if (showNoResults) {
                item {
                    Text(
                        "No results found",
                        color = Color.Gray,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            if (loadState is LoadState.Error && hasSearched.value) {
                item {
                    Text(
                        "An error occurred. Please try again.",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            if (loadState is LoadState.NotLoading && searchResults.itemCount > 0) {
                items(searchResults.itemCount) { index ->
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
    }
}
