package com.breens.whatsappstatussaver.statuses.domain

import android.net.Uri

data class Media(
    val thumbnailUri: Uri?,
    val videoUri: Uri?,
    val isVideo: Boolean,
)
