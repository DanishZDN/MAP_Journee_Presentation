package com.android.example.mobileapp_journeeg9.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.android.example.mobileapp_journeeg9.model.JournalEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaScreen(
    journalEntries: List<JournalEntry>,
    onMediaClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                tonalElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier
                            .size(48.dp),
                        shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        // Profile placeholder
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Media Files",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = paddingValues,
            modifier = modifier.fillMaxSize()
        ) {
            items(
                journalEntries.filter { it.imageUrl != null }
                    .map { it.imageUrl!! }
            ) { imageUrl ->
                MediaItem(
                    imageUrl = imageUrl,
                    onClick = { onMediaClick(imageUrl) }
                )
            }
        }
    }
}

@Composable
private fun MediaItem(
    imageUrl: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .aspectRatio(1f)
            .padding(1.dp)
            .clickable(onClick = onClick),
        tonalElevation = 1.dp
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
