package com.breens.whatsappstatussaver.save.domain

import com.breens.whatsappstatussaver.statuses.domain.Media

interface SaveImagesRepository {
    suspend fun saveImage(mediaFile: Media): Boolean
}