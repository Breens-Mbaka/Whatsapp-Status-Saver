package com.breens.whatsappstatussaver.common

import android.os.Environment
import android.util.Log
import java.io.File

fun getWAStatusesUsingFromNormalStorage(setStatusFiles: (files: List<File>, isDocumentStorage: Boolean) -> Unit) {
    try {
        val folder = File(
            Environment.getExternalStorageDirectory()
                .toString() + "/Android/media/com.whatsapp/WhatsApp/Media/.Statuses",
        )
        if (!folder.exists() || !folder.isDirectory) {
            Log.e("Error", "Directory does not exist or is not a directory")
            return
        }

        val files = folder.listFiles { _, name ->
            name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png")
        }

        if (files != null) {
            Log.e("Files", files.contentToString())
            setStatusFiles(files.toList(), false)
        } else {
            Log.e("Error", "No image files found in the directory")
        }
    } catch (e: Exception) {
        Log.e("Error", "Exception: ${e.message}")
        e.printStackTrace()
    }
}
