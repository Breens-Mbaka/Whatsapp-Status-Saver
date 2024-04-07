package com.breens.whatsappstatussaver.statuses.presentation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.breens.whatsappstatussaver.mediafilepermission.mediaFolderIntent
import com.breens.whatsappstatussaver.share.shareImage
import com.breens.whatsappstatussaver.share.shareVideo
import com.breens.whatsappstatussaver.statuses.presentation.components.FulImageDialog
import com.breens.whatsappstatussaver.statuses.presentation.components.StatusesGrid
import kotlinx.coroutines.flow.collectLatest

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun StatusesScreen(
    viewModel: StatusesViewModel = hiltViewModel(),
    playVideo: (Uri?) -> Unit,
) {
    val analytics = viewModel.analytics()
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent: Intent? = result.data
            if (intent != null) {
                viewModel.sendEvent(
                    event = StatusesScreenUiEvents.GetStatusImages(
                        uri = intent.data,
                        fromNormalStorage = false,
                    )
                )
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val intent = context.mediaFolderIntent()
            launcher.launch(intent)
        } else {
            viewModel.sendEvent(
                event = StatusesScreenUiEvents.GetStatusImages(
                    uri = null,
                    fromNormalStorage = true
                )
            )
        }

        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is StatusesScreenUiEvents.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(event.message)
                }

                is StatusesScreenUiEvents.GetStatusImages -> {
                    analytics.logEvent("get_status_images", null)
                }

                is StatusesScreenUiEvents.SaveMediaFile -> {
                    analytics.logEvent("save_image", null)
                }

                is StatusesScreenUiEvents.ChangeTab -> {
                    analytics.logEvent("change_tab", null)
                }

                is StatusesScreenUiEvents.ShareMediaFile -> {
                    if (event.mediaFile.isVideo) {
                        context.shareVideo(videoUri = event.mediaFile.videoUri)
                    } else {
                        context.shareImage(imageUri = event.mediaFile.thumbnailUri)
                    }
                }

                is StatusesScreenUiEvents.PlayVideo -> {
                    analytics.logEvent("play_video", null)
                    playVideo(event.videoUri)
                }

                else -> {}
            }
        }
    }

    val imagesUiState by viewModel.statusesScreenUiState.collectAsState()

    StatusesScreenContent(
        imagesUiState = imagesUiState,
        snackBarHostState = snackBarHostState,
        sendEvent = viewModel::sendEvent,
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun StatusesScreenContent(
    imagesUiState: StatusesScreenUiState,
    snackBarHostState: SnackbarHostState,
    sendEvent: (StatusesScreenUiEvents) -> Unit,
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                snackbar = {
                    Snackbar(
                        modifier = Modifier.padding(16.dp),
                        containerColor = MaterialTheme.colorScheme.surface,
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (imagesUiState.imageSavedSuccessfully) Icons.Rounded.CheckCircle else Icons.Rounded.Error,
                                contentDescription = null,
                                tint = if (imagesUiState.imageSavedSuccessfully) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                            )

                            Text(
                                text = it.visuals.message,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W500),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            )
        },
    ) { paddingValues ->
        if (imagesUiState.showFullImageDialog) {
            FulImageDialog(
                imageUri = imagesUiState.imageUriClicked,
                closeDialog = {
                    sendEvent(
                        StatusesScreenUiEvents.ShowFullImageDialog(
                            show = false,
                            imageUri = null
                        )
                    )
                }
            )
        }

        Column(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            TabRow(selectedTabIndex = imagesUiState.selectedTab) {
                imagesUiState.tabs.forEach { tabItem ->
                    Tab(
                        modifier = Modifier.padding(14.dp),
                        selected = imagesUiState.selectedTab == tabItem.index,
                        onClick = {
                            sendEvent(
                                StatusesScreenUiEvents.ChangeTab(
                                    tab = tabItem.index
                                )
                            )
                        }
                    ) {
                        Text(
                            text = tabItem.title,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                }
            }

            StatusesGrid(
                media = imagesUiState.media,
                saveMediaFile = { mediaFile ->
                    sendEvent(
                        StatusesScreenUiEvents.SaveMediaFile(
                            mediaFile = mediaFile
                        )
                    )
                },
                shareMediaFile = { mediaFile ->
                    sendEvent(
                        StatusesScreenUiEvents.ShareMediaFile(
                            mediaFile = mediaFile
                        )
                    )
                },
                onImageClicked = {
                    sendEvent(
                        StatusesScreenUiEvents.ShowFullImageDialog(
                            show = true,
                            imageUri = it
                        )
                    )
                },
                onVideoClicked = {
                    sendEvent(
                        StatusesScreenUiEvents.PlayVideo(
                            videoUri = it
                        )
                    )
                }
            )
        }
    }
}
