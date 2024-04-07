package com.breens.whatsappstatussaver.player.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Video(
    val videoUri: String,
) : Parcelable