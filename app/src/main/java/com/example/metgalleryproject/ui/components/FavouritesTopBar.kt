package com.example.metgalleryproject.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritesTopBar() {
    TopAppBar(
        title = {
            Text(
                text = "Favourite Artworks",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(top = 0.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF799485)
        )
    )
}