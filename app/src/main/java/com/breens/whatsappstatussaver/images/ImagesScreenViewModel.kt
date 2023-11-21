package com.breens.whatsappstatussaver.images

import android.net.Uri
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ImagesScreenViewModel @Inject constructor(private val analytics: FirebaseAnalytics) :
    ViewModel() {

    val filesState = MutableStateFlow<List<FileUiState>>(emptyList())

    fun setStatusFiles(
        documentFiles: List<DocumentFile> = emptyList(),
        files: List<File> = emptyList(),
        isDocumentStorage: Boolean,
    ) {
        val filteredDocumentFiles = documentFiles.filter {
            val fileName = it.name.orEmpty()
            fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(
                ".png",
            )
        }
        if (isDocumentStorage) {
            filesState.value =
                convertDocumentFilesToCommonModel(documentFiles = filteredDocumentFiles)
            return
        }

        val filteredFiles = files.filter {
            val fileName = it.name.orEmpty()
            fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(
                ".png",
            )
        }
        filesState.value = convertFilesToCommonModel(files = filteredFiles)
    }

    private fun convertFilesToCommonModel(files: List<File>): List<FileUiState> {
        return files.map { file ->
            FileUiState(
                fileName = file.name,
                filePath = file.toUri(),
                file = file,
            )
        }
    }

    private fun convertDocumentFilesToCommonModel(documentFiles: List<DocumentFile>): List<FileUiState> {
        return documentFiles.map { documentFile ->
            FileUiState(
                fileName = documentFile.name ?: "",
                filePath = documentFile.uri,
                documentFile = documentFile,
            )
        }
    }

    fun analytics() = analytics
}

data class FileUiState(
    val fileName: String,
    val filePath: Uri,
    val documentFile: DocumentFile? = null,
    val file: File? = null,
)
