package com.example.metgalleryproject.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.metgalleryproject.ui.components.BottomNavBar
import com.example.metgalleryproject.ui.components.TopNavBar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopNavBar()

        Spacer(modifier = Modifier.weight(1f))

        BottomNavBar(navController)
    }
}