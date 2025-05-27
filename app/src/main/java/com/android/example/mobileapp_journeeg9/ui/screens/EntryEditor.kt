package com.android.example.mobileapp_journeeg9.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.android.example.mobileapp_journeeg9.ui.util.*
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryEditor(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    imageUri: Uri?,
    onImagePicked: (Uri) -> Unit,
    location: LatLng?,
    onLocationSelected: (LatLng) -> Unit,
    onSave: () -> Unit,
    onDelete: (() -> Unit)? = null,
    onBack: () -> Unit,
    isEdit: Boolean = false,
    windowStateUtil: WindowStateUtil,
    modifier: Modifier = Modifier
) {
    val mediaPickerState = rememberMediaPickerState()
    val mediaPicker = rememberMediaPicker(onImagePicked)
    var showLocationPicker by remember { mutableStateOf(false) }

    if (mediaPickerState.showDialog) {
        mediaPicker.ShowMediaPickerDialog(
            onDismiss = { mediaPickerState.dismiss() }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEdit) "Edit Entry" else "New Entry") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (isEdit) {
                        IconButton(onClick = { onDelete?.invoke() }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                    IconButton(onClick = onSave) {
                        Icon(Icons.Default.Check, contentDescription = "Save")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (windowStateUtil.shouldShowTwoPanes) {
            // Tablet layout
            Row(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Left pane: Editor
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    EditorContent(
                        title = title,
                        onTitleChange = onTitleChange,
                        description = description,
                        onDescriptionChange = onDescriptionChange,
                        imageUri = imageUri,
                        onAddImage = { mediaPickerState.show() },
                        onAddLocation = { showLocationPicker = true }
                    )
                }

                // Right pane: Preview or Location picker
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    tonalElevation = 1.dp
                ) {
                    if (showLocationPicker) {
                        LocationPicker(
                            initialLocation = location,
                            onLocationSelected = { 
                                onLocationSelected(it)
                                showLocationPicker = false
                            },
                            onDismiss = { showLocationPicker = false }
                        )
                    } else {
                        // Preview
                        EntryPreview(
                            title = title,
                            description = description,
                            imageUri = imageUri,
                            location = location
                        )
                    }
                }
            }
        } else {
            // Phone layout
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                EditorContent(
                    title = title,
                    onTitleChange = onTitleChange,
                    description = description,
                    onDescriptionChange = onDescriptionChange,
                    imageUri = imageUri,
                    onAddImage = { mediaPickerState.show() },
                    onAddLocation = { showLocationPicker = true }
                )

                if (showLocationPicker) {
                    LocationPicker(
                        initialLocation = location,
                        onLocationSelected = { 
                            onLocationSelected(it)
                            showLocationPicker = false
                        },
                        onDismiss = { showLocationPicker = false }
                    )
                }
            }
        }
    }
}

@Composable
private fun EditorContent(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    imageUri: Uri?,
    onAddImage: () -> Unit,
    onAddLocation: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            minLines = 5
        )

        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onAddImage,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Image, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Media")
            }

            Button(
                onClick = onAddLocation,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Location")
            }
        }
    }
}

@Composable
private fun LocationPicker(
    initialLocation: LatLng?,
    onLocationSelected: (LatLng) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedLocation by remember { mutableStateOf(initialLocation) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            initialLocation ?: LatLng(0.0, 0.0),
            15f
        )
    }

    Column(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                selectedLocation = latLng
            }
        ) {
            selectedLocation?.let { location ->
                Marker(
                    state = MarkerState(position = location)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    selectedLocation?.let { onLocationSelected(it) }
                },
                enabled = selectedLocation != null
            ) {
                Text("Select Location")
            }
        }
    }
}

@Composable
private fun EntryPreview(
    title: String,
    description: String,
    imageUri: Uri?,
    location: LatLng?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Preview",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge
        )

        if (location != null) {
            Spacer(modifier = Modifier.height(16.dp))
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(location, 15f)
                }
            ) {
                Marker(
                    state = MarkerState(position = location)
                )
            }
        }
    }
}
