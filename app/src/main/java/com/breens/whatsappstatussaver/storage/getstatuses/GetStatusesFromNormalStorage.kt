package com.breens.whatsappstatussaver.storage.getstatuses

import android.os.Environment
import java.io.File

fun getStatusesFromNormalStorage(): List<File> {
    return try {
        val folder = File(
            Environment.getExternalStorageDirectory()
                .toString() + "/WhatsApp/Media/.Statuses",
        )

        if (folder.exists()) {
            return folder.listFiles()?.toList().orEmpty()
        }

        emptyList()
    } catch (e: Exception) {
        emptyList()
    }
}
