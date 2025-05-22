package com.example.metgalleryproject.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import com.example.metgalleryproject.data.model.ArtObject
import com.example.metgalleryproject.ui.components.CustomLoadingIndicator
import com.example.metgalleryproject.ui.components.FavouritesButton
import com.example.metgalleryproject.ui.components.TopNavBar
import com.example.metgalleryproject.viewmodel.FavouritesViewModel
import com.example.metgalleryproject.viewmodel.HomeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel,
    favouritesViewModel: FavouritesViewModel
) {
    val lazyPagingItems = viewModel.artObjectsFlow.collectAsLazyPagingItems()

    Column(modifier = Modifier.fillMaxSize()) {
        TopNavBar()
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            items(
                count = lazyPagingItems.itemCount,
                key = lazyPagingItems.itemKey { it.id }
            ) { index ->
                val art = lazyPagingItems[index]
                if (art != null) {
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
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CustomLoadingIndicator()
                    }
                }
            }

            when (val state = lazyPagingItems.loadState.refresh) {
                is androidx.paging.LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CustomLoadingIndicator()
                        }
                    }
                }
                is androidx.paging.LoadState.Error -> {
                    item { ErrorItem("Loading error: ${state.error.localizedMessage}") }
                }
                else -> {}
            }

            when (val state = lazyPagingItems.loadState.append) {
                is androidx.paging.LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CustomLoadingIndicator()
                        }
                    }
                }
                is androidx.paging.LoadState.Error -> {
                    item { ErrorItem("Loading error: ${state.error.localizedMessage}") }
                }
                else -> {}
            }
        }
    }
}

@Composable
fun ErrorItem(message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(message, color = Color.Gray)
    }
}

@Composable
fun ArtItem(art: ArtObject, navController: NavController, viewModel: FavouritesViewModel) {
    val painter = if (!art.imageUrl.isNullOrBlank()) {
        rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(art.imageUrl)
                .crossfade(true)
                .size(300)
                .scale(Scale.FILL)
                .build()
        )
    } else {
        null
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 8.dp)
            .clickable {
                navController.navigate("details/${art.id}")
            },
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (painter != null) {
            Image(
                painter = painter,
                contentDescription = art.title,
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxHeight()
            )
        } else {
            Box(
                modifier = Modifier
                    .weight(0.7f)
                    .height(100.dp)
                    .background(Color.LightGray)
                    .border(1.dp, Color.Gray)
            )
        }


        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1.3f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
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
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
