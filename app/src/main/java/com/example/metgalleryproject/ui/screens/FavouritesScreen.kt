package com.example.metgalleryproject.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.metgalleryproject.data.model.ArtObject
import com.example.metgalleryproject.ui.components.FavouritesTopBar
import com.example.metgalleryproject.viewmodel.FavouritesViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FavouritesScreen(navController: NavController, viewModel: FavouritesViewModel) {

    Column(modifier = Modifier.fillMaxSize()) {
        FavouritesTopBar()
        if (viewModel.favouriteArtObjects.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text(
                    text = "No artworks added",
                    color = Color.Gray,
                )
            }
        } else LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            items(viewModel.favouriteArtObjects) { art: ArtObject ->
                ArtItem(art = art, navController = navController, viewModel = viewModel)
                HorizontalDivider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}
