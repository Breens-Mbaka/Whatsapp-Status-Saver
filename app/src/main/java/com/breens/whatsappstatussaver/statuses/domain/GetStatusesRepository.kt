package com.breens.whatsappstatussaver.statuses.domain

import android.net.Uri

interface GetStatusesRepository {
    suspend fun getStatuses(uri: Uri?, fromNormalStorage: Boolean, mediaType: String): List<Media>
}