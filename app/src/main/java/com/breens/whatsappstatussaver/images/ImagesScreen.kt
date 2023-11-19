package com.breens.whatsappstatussaver.images

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.breens.whatsappstatussaver.common.askMediaFolderAccessPermission
import com.breens.whatsappstatussaver.common.getWAStatusesUsingFromNormalStorage
import com.breens.whatsappstatussaver.common.getWAStatusesUsingScopedStorage
import com.breens.whatsappstatussaver.common.saveImageForLegacyStorage
import com.breens.whatsappstatussaver.common.saveImageForScopedStorage
import com.breens.whatsappstatussaver.common.saveImageToDownloads
import com.breens.whatsappstatussaver.images.components.ImageStatus
import com.breens.whatsappstatussaver.ui.theme.PrimaryColor800
import com.breens.whatsappstatussaver.ui.theme.WhatsappStatusSaverTheme
import com.google.firebase.analytics.logEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagesScreen(
    imagesScreenViewModel: ImagesScreenViewModel = hiltViewModel(),
) {
    val analytics = imagesScreenViewModel.analytics()

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                getWAStatusesUsingScopedStorage(
                    uri = data.data!!,
                    context = context,
                    setStatusFiles = { files, isDocumentStorage ->
                        imagesScreenViewModel.setStatusFiles(
                            documentFiles = files,
                            isDocumentStorage = isDocumentStorage,
                        )
                    },
                )
            }
        }
    }

    val hasImageStoragePermission = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_MEDIA_IMAGES,
                ) == PackageManager.PERMISSION_GRANTED,
            )
        } else {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                ) == PackageManager.PERMISSION_GRANTED,
            )
        }
    }

    val imageStoragePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasImageStoragePermission.value = granted
        },
    )

    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_MEDIA_IMAGES,
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE,
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        hasImageStoragePermission.value = true
    }

    val files = imagesScreenViewModel.filesState.collectAsState()

    LaunchedEffect(key1 = true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            askMediaFolderAccessPermission(
                context = context,
                askPermission = { intent ->
                    launcher.launch(intent)
                },
            )
        } else {
            if (hasImageStoragePermission.value) {
                getWAStatusesUsingFromNormalStorage(
                    setStatusFiles = { files, isDocumentStorage ->
                        imagesScreenViewModel.setStatusFiles(
                            files = files.toList(),
                            isDocumentStorage = isDocumentStorage,
                        )
                    },
                )
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    imageStoragePermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                } else {
                    imageStoragePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }
    }

    WhatsappStatusSaverTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Status Saver",
                            style = MaterialTheme.typography.titleLarge,
                        )
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = PrimaryColor800,
                    ),
                )
            },
            containerColor = Color.White,
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                if (files.value.isNotEmpty()) {
                    ImageStatus(
                        images = files.value,
                        saveImageToDownloads = { imageUri ->
                            saveImageToDownloads(
                                saveImageForScopedStorage = {
                                    saveImageForScopedStorage(
                                        imageUri = imageUri,
                                        context = context,
                                        isImageSavedSuccessfully = { isSavedSuccessfully ->
                                            analytics.logEvent("IS_IMAGE_SAVED_SUCCESSFULLY") {
                                                param("SUCCESS", "$isSavedSuccessfully")
                                            }
                                        },
                                    )

                                    analytics.logEvent("IMAGE_STORAGE") {
                                        param("STORAGE", "Scoped Storage")
                                    }
                                },
                                saveImageForLegacyStorage = {
                                    saveImageForLegacyStorage(
                                        imageUri = imageUri,
                                        context = context,
                                    )

                                    analytics.logEvent("IMAGE_STORAGE") {
                                        param("STORAGE", "Legacy Storage")
                                    }
                                },
                            )
                        },
                    )
                }
            }
        }
    }
}
