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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.metgalleryproject.ui.components.SearchButton
import com.example.metgalleryproject.viewmodel.FavouritesViewModel
import com.example.metgalleryproject.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchScreen(navController: NavController, viewModel: SearchViewModel, favouritesViewModel: FavouritesViewModel) {
    val searchQuery = remember { mutableStateOf("") }
    val artObjects = viewModel.artObjects.collectAsState().value

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
                    viewModel.searchArtworks(searchQuery.value)
                },
            )
        }

            Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 0.dp)
            ) {
                items(artObjects) { art ->
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
