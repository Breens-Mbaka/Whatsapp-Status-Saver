package com.breens.whatsappstatussaver.mediafilepermission

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import androidx.annotation.RequiresApi
import androidx.documentfile.provider.DocumentFile


@RequiresApi(Build.VERSION_CODES.O)
fun Context.getMediaFolder(): DocumentFile? {
    val mediaFolder = DocumentFile.fromTreeUri(
        this,
        Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses"),
    )
    return mediaFolder
}

@RequiresApi(Build.VERSION_CODES.O)
fun checkIfMediaFolderAccessible(documentFile: DocumentFile): Boolean {
    return documentFile.canRead()
}


@RequiresApi(Build.VERSION_CODES.O)
fun Context.mediaFolderIntent(documentFile: DocumentFile): Intent {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        intent.putExtra(
            DocumentsContract.EXTRA_INITIAL_URI,
            documentFile.uri,
        )
        return intent
}