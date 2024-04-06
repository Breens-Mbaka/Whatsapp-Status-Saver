package com.breens.whatsappstatussaver.statuses.data

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.breens.whatsappstatussaver.storage.getstatuses.getStatusesFromScopedStorage
import com.breens.whatsappstatussaver.storage.getstatuses.getStatusesFromNormalStorage
import com.breens.whatsappstatussaver.statuses.domain.GetStatusImagesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetStatusImagesRepositoryImpl(@ApplicationContext private val context: Context) :
    GetStatusImagesRepository {
    override suspend fun getStatusImages(uri: Uri?, fromNormalStorage: Boolean): List<Uri> {
        return withContext(Dispatchers.IO) {
            if (fromNormalStorage) {
                val filesFromNormalStorage = getStatusesFromNormalStorage()

                val imageFromNormalStorage = filesFromNormalStorage.filter {
                    val fileName = it.name.orEmpty()
                    fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(
                        ".png",
                    )
                }

                return@withContext imageFromNormalStorage.map { file ->
                    file.toUri()
                }
            }
            val filesFromScopedStorage = getStatusesFromScopedStorage(
                uri = uri,
                context = context,
            )

            val imagesFromScopedStorage = filesFromScopedStorage.filter {
                val fileName = it.name.orEmpty()
                fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(
                    ".png",
                )
            }

            imagesFromScopedStorage.map { documentFile ->
                documentFile.uri
            }
        }
    }
}