package com.android.example.mobileapp_journeeg9.ui.util

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun rememberMediaPicker(
    onImagePicked: (Uri) -> Unit
): MediaPicker {
    val context = LocalContext.current
    return remember(context) {
        MediaPicker(context, onImagePicked)
    }
}

class MediaPicker(
    private val context: Context,
    private val onImagePicked: (Uri) -> Unit
) {
    private var tempImageUri: Uri? = null

    @Composable
    fun ShowMediaPickerDialog(
        onDismiss: () -> Unit
    ) {
        val takePhotoLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture()
        ) { success ->
            if (success) {
                tempImageUri?.let { uri ->
                    onImagePicked(uri)
                }
            }
            onDismiss()
        }

        val pickImageLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            uri?.let {
                onImagePicked(it)
            }
            onDismiss()
        }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Add Photo") },
            text = { Text("Choose a method to add a photo") },
            confirmButton = {
                TextButton(
                    onClick = {
                        tempImageUri = createImageUri()
                        tempImageUri?.let { uri ->
                            takePhotoLauncher.launch(uri)
                        }
                    }
                ) {
                    Text("Take Photo")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        pickImageLauncher.launch("image/*")
                    }
                ) {
                    Text("Choose from Gallery")
                }
            }
        )
    }

    private fun createImageUri(): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir = context.getExternalFilesDir("images")
        val imageFile = File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            imageFile
        )
    }
}

@Composable
fun rememberMediaPickerState(): MediaPickerState {
    return remember { MediaPickerState() }
}

class MediaPickerState {
    var showDialog by mutableStateOf(false)
        private set

    fun show() {
        showDialog = true
    }

    fun dismiss() {
        showDialog = false
    }
}
