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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.metgalleryproject.data.model.ArtObject
import com.example.metgalleryproject.ui.components.BottomNavBar
import com.example.metgalleryproject.ui.components.FavouritesButton
import com.example.metgalleryproject.ui.components.TopNavBar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavController, artObjects: List<ArtObject>) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopNavBar()

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            items(artObjects) { art ->
                ArtItem(art = art, navController = navController)
                HorizontalDivider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        BottomNavBar(navController)
    }
}

@Composable
fun ArtItem(art: ArtObject, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                // Navigare către DetailsScreen
                navController.navigate("details/${art.id}")
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(art.imageUrl),
            contentDescription = art.title,
            modifier = Modifier
                .size(100.dp)
                .padding(end = 16.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(text = art.title, style = MaterialTheme.typography.titleMedium)
            Text(text = "By ${art.artist}", style = MaterialTheme.typography.bodySmall)
        }

        FavouritesButton(art)
    }
}

