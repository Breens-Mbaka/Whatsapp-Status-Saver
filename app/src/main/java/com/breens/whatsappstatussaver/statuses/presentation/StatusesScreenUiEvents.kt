package com.breens.whatsappstatussaver.statuses.presentation

import android.net.Uri

sealed class StatusesScreenUiEvents {
    data class ShowSnackBar(val message: String) : StatusesScreenUiEvents()
    data class GetStatusImages(val uri: Uri?, val fromNormalStorage: Boolean) : StatusesScreenUiEvents()
    data class SaveImage(val imageUri: Uri) : StatusesScreenUiEvents()
    data class ChangeTab(val tab: Int) : StatusesScreenUiEvents()
}