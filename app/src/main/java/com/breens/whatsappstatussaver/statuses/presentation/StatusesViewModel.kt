package com.breens.whatsappstatussaver.statuses.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breens.whatsappstatussaver.save.domain.SaveImagesRepository
import com.breens.whatsappstatussaver.statuses.domain.GetStatusImagesRepository
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class StatusesViewModel @Inject constructor(
    private val getStatusImagesRepository: GetStatusImagesRepository,
    private val saveImagesRepository: SaveImagesRepository,
    private val analytics: FirebaseAnalytics,
) : ViewModel() {
    private val _statusesScreenUiState = MutableStateFlow(StatusesScreenUiState())
    val imagesUiState = _statusesScreenUiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<StatusesScreenUiEvents>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun sendEvent(event: StatusesScreenUiEvents) {
        viewModelScope.launch {
            when (event) {
                is StatusesScreenUiEvents.ShowSnackBar -> {
                    _eventFlow.emit(
                        StatusesScreenUiEvents.ShowSnackBar(message = event.message)
                    )
                }

                is StatusesScreenUiEvents.GetStatusImages -> {
                    getStatusImages(event.uri, event.fromNormalStorage)
                }

                is StatusesScreenUiEvents.SaveImage -> {
                    saveImage(event.imageUri)
                }

                is StatusesScreenUiEvents.ChangeTab -> {
                    changeTab(selectedTab = event.tab)
                }
            }
        }
    }

    private fun changeTab(selectedTab: Int) {
        _statusesScreenUiState.update {
            it.copy(
                selectedTab = selectedTab
            )
        }
    }

    private fun getStatusImages(uri: Uri?, fromNormalStorage: Boolean) {
        viewModelScope.launch {
            _statusesScreenUiState.update {
                it.copy(
                    isLoading = true
                )
            }

            val images = getStatusImagesRepository.getStatusImages(
                uri = uri,
                fromNormalStorage = fromNormalStorage,
            )

            _statusesScreenUiState.update {
                it.copy(
                    isLoading = false,
                    images = images
                )
            }
        }
    }

    private fun saveImage(imageUri: Uri) {
        viewModelScope.launch {
            _statusesScreenUiState.update {
                it.copy(
                    savingImage = true
                )
            }

            val isSavedSuccessfully = saveImagesRepository.saveImage(imageUri)

            if (isSavedSuccessfully) {
                _statusesScreenUiState.update {
                    it.copy(
                        savingImage = false,
                        imageSavedSuccessfully = true
                    )
                }
                return@launch _eventFlow.emit(
                    StatusesScreenUiEvents.ShowSnackBar(message = "Image saved successfully")
                )
            }

            _statusesScreenUiState.update {
                it.copy(
                    savingImage = false,
                    imageSavedSuccessfully = false
                )
            }
            _eventFlow.emit(
                StatusesScreenUiEvents.ShowSnackBar(message = "Failed to save image, try again.")
            )
        }
    }

    fun analytics() = analytics
}