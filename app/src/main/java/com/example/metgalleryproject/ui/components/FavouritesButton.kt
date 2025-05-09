package com.example.metgalleryproject.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metgalleryproject.data.model.ArtObject
import com.example.metgalleryproject.viewmodel.FavouritesViewModel

@Composable
fun FavouritesButton(art: ArtObject, viewModel: FavouritesViewModel = viewModel()) {
    val isFavorite = viewModel.favouriteArtObjects.value.contains(art)

    Button(
        onClick = {
            if (isFavorite) {
                viewModel.removeFromFavorites(art)
            } else {
                viewModel.addToFavorites(art)
            }
        },
        modifier = Modifier.padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = if (isFavorite) Color.Gray else Color.Red)
    ) {
        Text(text = if (isFavorite) "Remove from Favorites" else "Add to Favorites")
    }
}
