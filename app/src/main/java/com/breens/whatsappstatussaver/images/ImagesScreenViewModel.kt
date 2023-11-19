package com.breens.whatsappstatussaver.images

import android.net.Uri
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breens.whatsappstatussaver.common.UiEvents
import com.breens.whatsappstatussaver.data.PreferencesRepository
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ImagesScreenViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val analytics: FirebaseAnalytics,
) : ViewModel() {

    val filesState = MutableStateFlow<List<FileUiState>>(emptyList())

    val isPermissionGrantedState = MutableStateFlow(false)

    private val _uiEvents: MutableSharedFlow<UiEvents> = MutableSharedFlow()
    val uiEvents = _uiEvents.asSharedFlow()

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

    fun setIsPermissionGranted(permissionGranted: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setIsPermissionGranted(
                permissionGranted = permissionGranted,
            )
        }
    }

    fun getIsPermissionGranted() {
        Log.e("CALLED", "CALLED")
        viewModelScope.launch {
            preferencesRepository.getIsPermissionGranted()
                .collect { isPermissionGranted ->
                    isPermissionGrantedState.value = isPermissionGranted
                    Log.e("PERMISSION", "$isPermissionGranted")
                    when (isPermissionGranted) {
                        true -> {
                            getUri()
                        }

                        else -> {
                            Log.e("NAVIGATE", "ASK_PERMISSIONS")
                            _uiEvents.emit(
                                UiEvents.NavigateToAskPermissions,
                            )
                        }
                    }
                }
        }
    }

    fun setUri(uri: Uri) {
        viewModelScope.launch {
            preferencesRepository.setUri(
                uri = uri,
            )
        }
    }

    private fun getUri() {
        viewModelScope.launch {
            preferencesRepository.getUri().collect { uri ->
                _uiEvents.emit(
                    UiEvents.GetWhatsAppStatuses(uri = uri),
                )
            }
        }
    }

    private fun convertFilesToCommonModel(files: List<File>): List<FileUiState> {
        return files.map { file ->
            FileUiState(
                fileName = file.name,
                filePath = Uri.parse("/Android/media/com.whatsapp/WhatsApp/Media/.Statuses/${file.name}"),
            )
        }
    }

    private fun convertDocumentFilesToCommonModel(documentFiles: List<DocumentFile>): List<FileUiState> {
        return documentFiles.map { documentFile ->
            FileUiState(
                fileName = documentFile.name ?: "",
                filePath = documentFile.uri,
            )
        }
    }

    fun analytics() = analytics
}

data class FileUiState(
    val fileName: String,
    val filePath: Uri,
)
