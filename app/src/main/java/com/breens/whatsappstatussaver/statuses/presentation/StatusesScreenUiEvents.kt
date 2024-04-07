package com.breens.whatsappstatussaver.statuses.presentation

import android.net.Uri
import com.breens.whatsappstatussaver.statuses.domain.Media

sealed class StatusesScreenUiEvents {
    data class ShowSnackBar(val message: String) : StatusesScreenUiEvents()
    data class GetStatusImages(val uri: Uri?, val fromNormalStorage: Boolean) : StatusesScreenUiEvents()
    data class SaveMediaFile(val mediaFile: Media) : StatusesScreenUiEvents()
    data class ShareMediaFile(val mediaFile: Media) : StatusesScreenUiEvents()
    data class ChangeTab(val tab: Int) : StatusesScreenUiEvents()
    data class ShowFullImageDialog(val show: Boolean, val imageUri: Uri?) : StatusesScreenUiEvents()
}