package com.example.metgalleryproject.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.metgalleryproject.data.model.ArtObject
import com.example.metgalleryproject.ui.components.FavouritesButton
import com.example.metgalleryproject.viewmodel.FavouritesViewModel
import com.example.metgalleryproject.viewmodel.HomeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel, favouritesViewModel: FavouritesViewModel) {

    LaunchedEffect(Unit) {
        viewModel.fetchArtObjects("https://collectionapi.metmuseum.org/public/collection/v1/objects/")
    }

    val artObjects = viewModel.artObjects

    val viewModel: FavouritesViewModel = viewModel()
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 0.dp)
        ) {
            items(artObjects) { art ->
                ArtItem(art = art, navController = navController, viewModel = favouritesViewModel)
                HorizontalDivider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun ArtItem(art: ArtObject, navController: NavController, viewModel: FavouritesViewModel) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 8.dp, start = 5.dp, end = 5.dp)
            .clickable {
                navController.navigate("details/${art.id}")
            }
    ) {

        Image(
            painter = rememberAsyncImagePainter(art.imageUrl),
            contentDescription = art.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = art.title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(1f)
                )
                FavouritesButton(art = art, viewModel = viewModel)
            }
            Spacer(modifier = Modifier.height(4.dp))
            val authorText = if (art.artist.isEmpty()) "Unknown" else "By ${art.artist}"

            Text(
                text = authorText,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal)
            )
        }
    }
}

