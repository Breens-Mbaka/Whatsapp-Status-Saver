package com.breens.whatsappstatussaver.statuses.data

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.breens.whatsappstatussaver.storage.getstatuses.getStatusesFromScopedStorage
import com.breens.whatsappstatussaver.storage.getstatuses.getStatusesFromNormalStorage
import com.breens.whatsappstatussaver.statuses.domain.GetStatusesRepository
import com.breens.whatsappstatussaver.statuses.domain.Media
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetStatusesRepositoryImpl(@ApplicationContext private val context: Context) :
    GetStatusesRepository {
    override suspend fun getStatuses(
        uri: Uri?,
        fromNormalStorage: Boolean,
        mediaType: String,
    ): List<Media> {
        return withContext(Dispatchers.IO) {
            if (fromNormalStorage) {
                val filesFromNormalStorage = getStatusesFromNormalStorage()

                return@withContext filesFromNormalStorage.filterFilesFromNormalStorageByMediaType(
                    mediaType = mediaType,
                )
            }

            val filesFromScopedStorage = getStatusesFromScopedStorage(
                uri = uri,
                context = context,
            )

            filesFromScopedStorage.filterFilesFromScopedStorageByMediaType(
                mediaType = mediaType,
            )
        }
    }

    private fun List<File>.filterFilesFromNormalStorageByMediaType(
        mediaType: String,
    ): List<Media> {
        val filteredFiles = this.filter { file ->
            when (mediaType) {
                "image" -> {
                    file.name.orEmpty().endsWith(".jpg") || file.name.orEmpty()
                        .endsWith(".jpeg") || file.name.orEmpty().endsWith(
                        ".png",
                    )
                }

                "video" -> {
                    file.name.orEmpty().endsWith(".mp4")
                }

                else -> {
                    false
                }
            }
        }

        val filesUri = filteredFiles.map { file ->
            val thumbnailUri = if (mediaType == "video") {
                file.toUri().createVideoThumbnail(context)
            } else {
                file.toUri()
            }

            val videoUri = if (mediaType == "video") {
                file.toUri()
            } else {
                null
            }

            Media(
                thumbnailUri = thumbnailUri,
                videoUri = videoUri,
                isVideo = mediaType == "video",
            )
        }

        return filesUri.toSet().toList()
    }

    private fun List<DocumentFile>.filterFilesFromScopedStorageByMediaType(
        mediaType: String,
    ): List<Media> {
        val filteredFiles = this.filter { file ->
            when (mediaType) {
                "image" -> {
                    file.name.orEmpty().endsWith(".jpg") || file.name.orEmpty()
                        .endsWith(".jpeg") || file.name.orEmpty().endsWith(
                        ".png",
                    )
                }

                "video" -> {
                    file.name.orEmpty().endsWith(".mp4")
                }

                else -> {
                    false
                }
            }
        }

        val filesUri = filteredFiles.map { file ->
            val thumbnailUri = if (mediaType == "video") {
                file.uri.createVideoThumbnail(context)
            } else {
                file.uri
            }

            val videoUri = if (mediaType == "video") {
                file.uri
            } else {
                null
            }

            Media(
                thumbnailUri = thumbnailUri,
                videoUri = videoUri,
                isVideo = mediaType == "video",
            )
        }

        return filesUri.toSet().toList()
    }

    private fun Uri.createVideoThumbnail(context: Context): Uri? {
        try {
            val mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(context, this)
            val bitmap = mediaMetadataRetriever.frameAtTime

            val timeStamp = System.currentTimeMillis()

            // Save the bitmap to a file
            val thumbnailFile = File(context.cacheDir, "thumbnail_$timeStamp.jpg")
            val fileOutputStream = FileOutputStream(thumbnailFile)
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.close()

            // Return the URI of the saved file
            return Uri.fromFile(thumbnailFile)
        } catch (ex: Exception) {
            Log.e("CreatePostViewModel", "Error retrieving video thumbnail", ex)
        }
        return null
    }
}