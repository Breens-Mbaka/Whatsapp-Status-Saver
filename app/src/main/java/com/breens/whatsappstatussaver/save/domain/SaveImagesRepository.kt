package com.breens.whatsappstatussaver.save.domain

import android.net.Uri

interface SaveImagesRepository {
    suspend fun saveImage(imageUri: Uri): Boolean
}