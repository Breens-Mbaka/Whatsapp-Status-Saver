package com.breens.whatsappstatussaver.share

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log

fun Context.shareImage(imageUri: Uri?) {
    Log.e("ShareContent", "shareImage: $imageUri")
    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, imageUri)
        type = "image/jpeg"
    }
    this.startActivity(Intent.createChooser(intent, null))
}

fun Context.shareVideo(videoUri: Uri?) {
    Log.e("ShareContent", "shareVideo: $videoUri")
    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, videoUri)
        type = "video/mp4"
    }
    this.startActivity(Intent.createChooser(intent, null))
}