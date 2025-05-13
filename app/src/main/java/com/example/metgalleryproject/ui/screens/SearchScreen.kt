package com.example.metgalleryproject.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.metgalleryproject.ui.components.SearchButton
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

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchQuery.value,
                onValueChange = { searchQuery.value = it },
                label = { Text("Search for art") },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                singleLine = true
            )

            SearchButton(
                onClick = {
                    viewModel.search(searchQuery.value)
                },
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
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

            searchResults.apply {
                when {
                    loadState.refresh is androidx.paging.LoadState.Loading ||
                            loadState.append is androidx.paging.LoadState.Loading -> {
                        item {
                            Text("Loading...", modifier = Modifier.padding(16.dp))
                        }
                    }
                    loadState.refresh is androidx.paging.LoadState.Error -> {
                        item {
                            Text("Error loading data", color = Color.Red, modifier = Modifier.padding(16.dp))
                        }
                    }
                }
            }
        }
    }
}
