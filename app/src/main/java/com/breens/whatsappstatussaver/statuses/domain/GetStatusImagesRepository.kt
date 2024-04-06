package com.breens.whatsappstatussaver.statuses.domain

import android.net.Uri

interface GetStatusImagesRepository {
    suspend fun getStatusImages(uri: Uri?, fromNormalStorage: Boolean): List<Uri>
}