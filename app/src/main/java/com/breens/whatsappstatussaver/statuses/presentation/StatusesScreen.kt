package com.breens.whatsappstatussaver.statuses.presentation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
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
import com.breens.whatsappstatussaver.mediafilepermission.mediaFolderIntent
import com.breens.whatsappstatussaver.share.shareImage
import com.breens.whatsappstatussaver.share.shareVideo
import com.breens.whatsappstatussaver.statuses.presentation.components.EmptyComponent
import com.breens.whatsappstatussaver.statuses.presentation.components.FulImageDialog
import com.breens.whatsappstatussaver.statuses.presentation.components.HelpInstructionsDialog
import com.breens.whatsappstatussaver.statuses.presentation.components.LoadingAnimation
import com.breens.whatsappstatussaver.statuses.presentation.components.StatusesGrid
import kotlinx.coroutines.flow.collectLatest

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun StatusesScreen(
    viewModel: StatusesViewModel,
    playVideo: (Uri?) -> Unit,
    savedUri: Uri?,
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

    val mediaPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { isGranted ->
        if (isGranted.all { it.value }) {
            viewModel.sendEvent(
                event = StatusesScreenUiEvents.GetStatusImages(
                    uri = null,
                    fromNormalStorage = true
                )
            )
        } else {
            viewModel.sendEvent(
                event = StatusesScreenUiEvents.ShowSnackBar(
                    message = "Permission denied"
                )
            )
        }
    }

    LaunchedEffect(key1 = Unit) {
        Log.e("SAVED_URI", "$savedUri")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val intent = context.mediaFolderIntent()
            launcher.launch(intent)
        } else {
            mediaPermissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.READ_MEDIA_IMAGES,
                    android.Manifest.permission.READ_MEDIA_VIDEO,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
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

    val statusesScreenUiState by viewModel.statusesScreenUiState.collectAsState()

    StatusesScreenContent(
        statusesScreenUiState = statusesScreenUiState,
        snackBarHostState = snackBarHostState,
        sendEvent = viewModel::sendEvent,
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun StatusesScreenContent(
    statusesScreenUiState: StatusesScreenUiState,
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
                                imageVector = if (statusesScreenUiState.imageSavedSuccessfully) Icons.Rounded.CheckCircle else Icons.Rounded.Error,
                                contentDescription = null,
                                tint = if (statusesScreenUiState.imageSavedSuccessfully) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
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
        if (statusesScreenUiState.showFullImageDialog) {
            FulImageDialog(
                imageUri = statusesScreenUiState.imageUriClicked,
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

        if (statusesScreenUiState.helpInstructionsIsDialogOpen) {
            HelpInstructionsDialog(
                closeDialog = {
                    sendEvent(
                        StatusesScreenUiEvents.ShowHelpInstructions(
                            show = false
                        )
                    )
                }
            )
        }

        if (statusesScreenUiState.isLoading) {
            LoadingAnimation()
        }

        if (!statusesScreenUiState.isLoading && statusesScreenUiState.media.isEmpty()) {
            EmptyComponent(
                showHelpInstructions = {
                    sendEvent(
                        StatusesScreenUiEvents.ShowHelpInstructions(
                            show = true
                        )
                    )
                }
            )
        }

        if (!statusesScreenUiState.isLoading && statusesScreenUiState.media.isNotEmpty()) {
            Column(
                modifier = Modifier.padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                TabRow(selectedTabIndex = statusesScreenUiState.selectedTab) {
                    statusesScreenUiState.tabs.forEach { tabItem ->
                        Tab(
                            modifier = Modifier.padding(14.dp),
                            selected = statusesScreenUiState.selectedTab == tabItem.index,
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
                    media = statusesScreenUiState.media,
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
}
