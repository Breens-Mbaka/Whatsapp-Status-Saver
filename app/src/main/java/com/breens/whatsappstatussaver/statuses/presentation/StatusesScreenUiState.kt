package com.breens.whatsappstatussaver.statuses.presentation

import android.net.Uri

data class StatusesScreenUiState(
    val isLoading: Boolean = false,
    val images: List<Uri> = emptyList(),
    val savingImage: Boolean = false,
    val imageSavedSuccessfully: Boolean = false,
    val selectedTab: Int = 0,
    val tabs: List<TabItem> = tabList,
)

val tabList = listOf(
    TabItem(0, "Images"),
    TabItem(1, "Videos"),

)
data class TabItem(
    val index: Int,
    val title: String,
)