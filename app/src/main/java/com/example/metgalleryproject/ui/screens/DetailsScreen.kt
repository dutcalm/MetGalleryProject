package com.example.metgalleryproject.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.metgalleryproject.ui.components.FavouritesButton
import com.example.metgalleryproject.ui.components.TopNavBar
import com.example.metgalleryproject.viewmodel.DetailsViewModel
import com.example.metgalleryproject.viewmodel.FavouritesViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailsScreen(artId: Int, viewModel: DetailsViewModel = viewModel(), favouritesViewModel: FavouritesViewModel) {

    val artDetails by viewModel.artDetails.collectAsState()

    LaunchedEffect(artId) {
        Log.d("DetailsScreen", "Art ID received: $artId")
        viewModel.fetchArtDetails(artId)
    }

    val openDialog = remember { mutableStateOf(false) }
    val selectedImageIndex = remember { mutableStateOf(0) }

    Column {
        TopNavBar()
    artDetails?.let { art ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp, 0.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = art.imageUrl,
                contentDescription = art.title,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 1.dp)
                    .padding(top = 23.dp)
            )

            Column(modifier = Modifier.padding(0.dp, 16.dp)) {

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = art.title,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f)
                    )
                    FavouritesButton(art = art, viewModel = favouritesViewModel)
                }

                Text(
                    text = "By ${if (art.artist.isEmpty()) "Unknown" else art.artist}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )

                Text(
                    text = "Year: ${if (art.year.isNullOrEmpty()) "Not available" else art.year}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )

                Text(
                    text = "Medium: ${if (art.medium.isNullOrEmpty()) "Not specified" else art.medium}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )

                Text(
                    text = "Dimensions: ${if (art.dimensions.isNullOrEmpty()) "Not available" else art.dimensions}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )

                Text(
                    text = "Department: ${if (art.department.isNullOrEmpty()) "Not available" else art.department}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )

                Text(
                    text = "Country: ${if (art.country.isNullOrEmpty()) "Not available" else art.country}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )

                if (art.additionalImages.isNotEmpty()) {
                    Column(modifier = Modifier.padding(top = 16.dp)) {
                        Text(
                            text = "Additional Images",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        )

                        LazyRow(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(art.additionalImages) { imageUrl ->
                                AsyncImage(
                                    model = imageUrl,
                                    contentDescription = "Additional Image",
                                    modifier = Modifier
                                        .padding(end = 8.dp)
                                        .height(150.dp)
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedImageIndex.value =
                                                art.additionalImages.indexOf(imageUrl)
                                            openDialog.value = true
                                        }
                                )
                            }
                        }
                    }
                }
            }
        }
            if (openDialog.value) {
                FullScreenImageDialog(
                    imageUrls = art.additionalImages,
                    currentIndex = selectedImageIndex.value,
                    onDismiss = { openDialog.value = false },
                    onPrevious = {
                        if (selectedImageIndex.value > 0) {
                            selectedImageIndex.value -= 1
                        }
                    },
                    onNext = {
                        if (selectedImageIndex.value < art.additionalImages.size - 1) {
                            selectedImageIndex.value += 1
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun FullScreenImageDialog(imageUrls: List<String>, currentIndex: Int, onDismiss: () -> Unit, onPrevious: () -> Unit, onNext: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnClickOutside = true)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = onPrevious,
                        enabled = currentIndex > 0
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Previous image",
                            modifier = Modifier.size(48.dp),
                            tint = if (currentIndex > 0) Color.White else Color.Gray
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = imageUrls[currentIndex],
                            contentDescription = "Full-screen image",
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    IconButton(
                        onClick = onNext,
                        enabled = currentIndex < imageUrls.size - 1
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Next image",
                            modifier = Modifier.size(48.dp),
                            tint = if (currentIndex < imageUrls.size - 1) Color.White else Color.Gray
                        )
                    }
                }
            }
        }
    }
}