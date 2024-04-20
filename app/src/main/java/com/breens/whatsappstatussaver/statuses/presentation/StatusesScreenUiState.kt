package com.breens.whatsappstatussaver.statuses.presentation

import android.net.Uri
import com.breens.whatsappstatussaver.statuses.domain.Media

data class StatusesScreenUiState(
    val isLoading: Boolean = false,
    val uri: Uri? = null,
    val fromNormalStorage: Boolean = false,
    val media: List<Media> = emptyList(),
    val savingImage: Boolean = false,
    val imageSavedSuccessfully: Boolean = false,
    val selectedTab: Int = 0,
    val tabs: List<TabItem> = tabList,
    val showFullImageDialog: Boolean = false,
    val imageUriClicked: Uri? = null,
    val helpInstructionsIsDialogOpen: Boolean = false,
)

val tabList = listOf(
    TabItem(0, "Images"),
    TabItem(1, "Videos"),

)
data class TabItem(
    val index: Int,
    val title: String,
)