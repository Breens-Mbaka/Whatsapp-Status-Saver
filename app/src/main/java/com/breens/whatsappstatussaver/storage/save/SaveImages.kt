package com.breens.whatsappstatussaver.storage.save

import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileOutputStream

fun Context.saveImage(imageUri: Uri): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        saveImageForScopedStorage(imageUri = imageUri, context = this)
    } else {
        saveImageForLegacyStorage(imageUri = imageUri, context = this)
    }
}

fun saveImageForLegacyStorage(imageUri: Uri, context: Context): Boolean {
    return try {
        val downloadsFolder =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val timeStamp = System.currentTimeMillis()
        val destinationFile = File(downloadsFolder, "whatsapp_status_saver_image_$timeStamp.jpg")

        context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
            FileOutputStream(destinationFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        // Notify MediaStore about the new file
        MediaScannerConnection.scanFile(
            context,
            arrayOf(destinationFile.absolutePath),
            null,
            null,
        )
        true
    } catch (e: Exception) {
        false
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
fun saveImageForScopedStorage(
    imageUri: Uri,
    context: Context,
): Boolean {
    return try {
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
            return true
        }
        false
    } catch (e: Exception) {
        false
    }
}