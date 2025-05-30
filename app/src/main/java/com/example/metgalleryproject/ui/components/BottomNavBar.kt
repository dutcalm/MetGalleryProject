package com.example.metgalleryproject.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavBar(navController: NavController) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp),
        containerColor = Color(0xFF0B1215),
        contentColor = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = { navController.navigate("home") }) {
                Icon(
                    Icons.Filled.Home,
                    contentDescription = "Home",
                    tint = if (currentRoute == "home") Color(0xFF799485) else Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            IconButton(onClick = { navController.navigate("search") }) {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = if (currentRoute == "search") Color(0xFF799485) else Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            IconButton(onClick = { navController.navigate("favourites") }) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "Favourites",
                    tint = if (currentRoute == "favourites") Color(0xFF799485) else Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}
