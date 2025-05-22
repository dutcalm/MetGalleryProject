package com.example.metgalleryproject.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun CustomLoadingIndicator(
    modifier: Modifier = Modifier
        .fillMaxWidth(0.8f)
        .height(700.dp)
) {
    var currentProgress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            for (i in 0..100) {
                currentProgress = i / 100f
                delay(20)
            }
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
            CircularProgressIndicator(
            progress = { currentProgress },
            modifier = Modifier
                                .height(64.dp)
                                .fillMaxWidth(0.3f),
            color = Color.Gray,
            strokeWidth = 10.dp,
            trackColor = Color.DarkGray,
            strokeCap = StrokeCap.Round,
            )
    }
}