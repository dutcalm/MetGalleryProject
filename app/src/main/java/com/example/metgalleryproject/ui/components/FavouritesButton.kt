package com.example.metgalleryproject.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.metgalleryproject.data.model.ArtObject
import com.example.metgalleryproject.viewmodel.FavouritesViewModel

@Composable
fun FavouritesButton(
    art: ArtObject,
    viewModel: FavouritesViewModel
) {
    val isFavorite = viewModel.isFavorite(art)

    IconButton(
        onClick = {
            if (isFavorite) {
                viewModel.removeFromFavorites(art)
            } else {
                viewModel.addToFavorites(art)
            }
        },
        modifier = Modifier.padding(8.dp)
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = if (isFavorite) "Favorite" else "Not Favorite",
            tint = if (isFavorite) Color(0xFFcf78a8) else Color.White,
            modifier = Modifier.size(35.dp)
        )
    }
}

