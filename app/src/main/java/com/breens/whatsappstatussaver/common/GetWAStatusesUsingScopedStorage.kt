package com.breens.whatsappstatussaver.common

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile

fun getWAStatusesUsingScopedStorage(
    uri: Uri,
    context: Context,
    setStatusFiles: (files: List<DocumentFile>, isDocumentStorage: Boolean) -> Unit,
) {
    val docFile = DocumentFile.fromTreeUri(context, uri)
    val files = docFile?.listFiles()

    setStatusFiles(
        files?.toList().orEmpty(),
        true,
    )
}
