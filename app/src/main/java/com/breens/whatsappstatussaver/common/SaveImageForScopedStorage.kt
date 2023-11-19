package com.breens.whatsappstatussaver.common

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.Q)
fun saveImageForScopedStorage(
    imageUri: Uri,
    context: Context,
    isImageSavedSuccessfully: (Boolean) -> Unit,
) {
    val resolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, "whatsapp_status_image.jpg")
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
    }

    val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
    uri?.let { newUri ->
        resolver.openOutputStream(newUri)?.use { outputStream ->
            resolver.openInputStream(imageUri)?.use { inputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        Toast.makeText(context, "Image saved successfully", Toast.LENGTH_SHORT).show()
        isImageSavedSuccessfully(true)
        return
    }

    isImageSavedSuccessfully(false)
    Toast.makeText(context, "Something went wrong when saving image", Toast.LENGTH_SHORT).show()
}
