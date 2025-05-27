package com.android.example.mobileapp_journeeg9.ui.screens

import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.android.example.mobileapp_journeeg9.model.JournalEntry

data class JournalMarker(
    val position: LatLng,
    val entry: JournalEntry
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AtlasScreen(
    journalMarkers: List<JournalMarker>,
    onMarkerClick: (JournalEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    
    var showPermissionDialog by remember { mutableStateOf(false) }
    
    val jakarta = LatLng(-6.2088, 106.8456)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(jakarta, 12f)
    }

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
                        text = "Atlas",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = locationPermissionState.hasPermission,
                    mapType = MapType.NORMAL
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    myLocationButtonEnabled = true,
                    mapToolbarEnabled = true,
                    compassEnabled = true
                )
            ) {
                journalMarkers.forEach { marker ->
                    Marker(
                        state = MarkerState(position = marker.position),
                        title = marker.entry.title,
                        snippet = marker.entry.description,
                        onClick = {
                            onMarkerClick(marker.entry)
                            true
                        }
                    )
                }
            }

            if (!locationPermissionState.hasPermission && !showPermissionDialog) {
                showPermissionDialog = true
                AlertDialog(
                    onDismissRequest = { showPermissionDialog = false },
                    title = { Text("Location Permission Required") },
                    text = { Text("This app needs location permission to show your location on the map.") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                locationPermissionState.launchPermissionRequest()
                                showPermissionDialog = false
                            }
                        ) {
                            Text("Grant Permission")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showPermissionDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}
