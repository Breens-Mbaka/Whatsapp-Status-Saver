package com.breens.whatsappstatussaver.storage.getstatuses

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile

fun getStatusesFromScopedStorage(
    uri: Uri?,
    context: Context,
): List<DocumentFile> {
    val docFile = uri?.let { DocumentFile.fromTreeUri(context, it) }
    return docFile?.listFiles()?.toList().orEmpty()
}
