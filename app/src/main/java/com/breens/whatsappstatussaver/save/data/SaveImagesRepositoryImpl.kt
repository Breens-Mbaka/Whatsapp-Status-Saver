package com.breens.whatsappstatussaver.save.data

import android.content.Context
import com.breens.whatsappstatussaver.save.domain.SaveImagesRepository
import com.breens.whatsappstatussaver.statuses.domain.Media
import com.breens.whatsappstatussaver.storage.save.saveMediaFile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveImagesRepositoryImpl(@ApplicationContext val context: Context) : SaveImagesRepository {
    override suspend fun saveImage(mediaFile: Media): Boolean {
        return withContext(Dispatchers.IO) {
            return@withContext context.saveMediaFile(mediaFile = mediaFile)
        }
    }
}