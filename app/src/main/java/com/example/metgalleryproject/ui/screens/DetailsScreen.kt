package com.example.metgalleryproject.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.metgalleryproject.ui.components.BottomNavBar
import com.example.metgalleryproject.ui.components.TopNavBar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailsScreen(navController: NavController) {

    TopNavBar()

    Spacer(modifier = Modifier.fillMaxHeight())

    BottomNavBar(navController)
}