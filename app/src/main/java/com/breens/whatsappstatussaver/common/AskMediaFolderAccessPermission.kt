package com.breens.whatsappstatussaver.common

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import androidx.documentfile.provider.DocumentFile


@SuppressLint("InlinedApi")
fun askMediaFolderAccessPermission(context: Context, askPermission: (Intent) -> Unit) {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
    intent.putExtra(
        DocumentsContract.EXTRA_INITIAL_URI,
        DocumentFile.fromTreeUri(
            context,
            Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses"),
        )!!.uri,
    )
    askPermission(intent)
}