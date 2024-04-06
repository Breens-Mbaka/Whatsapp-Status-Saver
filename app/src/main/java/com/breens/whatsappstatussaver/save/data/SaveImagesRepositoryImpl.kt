package com.breens.whatsappstatussaver.save.data

import android.content.Context
import android.net.Uri
import com.breens.whatsappstatussaver.save.domain.SaveImagesRepository
import com.breens.whatsappstatussaver.storage.save.saveImage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveImagesRepositoryImpl(@ApplicationContext val context: Context) : SaveImagesRepository {
    override suspend fun saveImage(imageUri: Uri): Boolean {
        return withContext(Dispatchers.IO) {
            context.saveImage(imageUri = imageUri)
        }
    }
}