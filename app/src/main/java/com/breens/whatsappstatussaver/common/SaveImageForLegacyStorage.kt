package com.breens.whatsappstatussaver.common

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun saveImageForLegacyStorage(imageUri: Uri, context: Context) {
    val downloadsFolder =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val timeStamp = System.currentTimeMillis()
    val destinationFile = File(downloadsFolder, "whatsapp_status_saver_image_$timeStamp.jpg")

    try {
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

        Toast.makeText(context, "Image saved successfully", Toast.LENGTH_SHORT).show()
    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(context, "Something went wrong when saving image", Toast.LENGTH_SHORT).show()
    }
}
