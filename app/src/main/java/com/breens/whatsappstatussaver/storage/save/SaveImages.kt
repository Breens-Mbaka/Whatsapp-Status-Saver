package com.breens.whatsappstatussaver.storage.save

import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.breens.whatsappstatussaver.statuses.domain.Media
import java.io.File
import java.io.FileOutputStream

fun Context.saveMediaFile(mediaFile: Media): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        saveMediaFileForScopedStorage(mediaFile = mediaFile, context = this)
    } else {
        saveMediaFileForLegacyStorage(mediaFile = mediaFile, context = this)
    }
}

fun saveMediaFileForLegacyStorage(mediaFile: Media, context: Context): Boolean {
    return try {
        val mediaUri = if (mediaFile.isVideo) mediaFile.videoUri else mediaFile.thumbnailUri

        if (mediaUri != null) {
            val downloadsFolder =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val timeStamp = System.currentTimeMillis()
            val mimeType = if (mediaFile.isVideo) ".mp4" else ".jpeg"
            val destinationFile =
                File(downloadsFolder, "whatsapp_status_saver_$timeStamp$mimeType")

            context.contentResolver.openInputStream(mediaUri)?.use { inputStream ->
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
            return true
        }

        false
    } catch (e: Exception) {
        false
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
fun saveMediaFileForScopedStorage(
    mediaFile: Media,
    context: Context,
): Boolean {
    return try {
        val mediaUri = if (mediaFile.isVideo) mediaFile.videoUri else mediaFile.thumbnailUri

        if (mediaUri != null) {
            val resolver = context.contentResolver
            val timeStamp = System.currentTimeMillis()
            val mimeType = if (mediaFile.isVideo) "video/mp4" else "image/jpeg"
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "whatsapp_status_saver_$timeStamp")
                put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            uri?.let { newUri ->
                resolver.openOutputStream(newUri)?.use { outputStream ->
                    resolver.openInputStream(mediaUri)?.use { inputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                return true
            }
        }
        false
    } catch (e: Exception) {
        false
    }
}