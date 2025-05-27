package com.android.example.mobileapp_journeeg9

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.android.example.mobileapp_journeeg9.navigation.JournalNavigation
import com.android.example.mobileapp_journeeg9.ui.theme.MobileApp_JourneeG9Theme
import com.android.example.mobileapp_journeeg9.ui.util.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalPermissionsApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            val windowStateUtil = rememberWindowStateUtil(windowSizeClass)
            val devicePosture = rememberDevicePosture()
            
            val permissionsState = rememberMultiplePermissionsState(
                permissions = listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
            
            var showPermissionDialog by remember { mutableStateOf(!permissionsState.allPermissionsGranted) }

            MobileApp_JourneeG9Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (showPermissionDialog) {
                        AlertDialog(
                            onDismissRequest = { },
                            title = { Text("Permissions Required") },
                            text = {
                                Text(
                                    "This app requires location, camera, and storage permissions " +
                                    "to provide full functionality. Please grant the permissions."
                                )
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        permissionsState.launchMultiplePermissionRequest()
                                        showPermissionDialog = false
                                    }
                                ) {
                                    Text("Grant Permissions")
                                }
                            }
                        )
                    }

                    val navController = rememberNavController()
                    
                    if (devicePosture.isLandscape && windowStateUtil.shouldShowTwoPanes) {
                        // Tablet landscape layout with navigation rail and two panes
                        Row(modifier = Modifier.fillMaxSize()) {
                            NavigationRail {
                                // Navigation items
                            }
                            JournalNavigation(
                                navController = navController,
                                windowStateUtil = windowStateUtil
                            )
                        }
                    } else {
                        // Phone layout or tablet portrait
                        JournalNavigation(
                            navController = navController,
                            windowStateUtil = windowStateUtil
                        )
                    }
                }
            }
        }
    }
}
