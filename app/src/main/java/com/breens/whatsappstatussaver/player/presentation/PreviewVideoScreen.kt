package com.breens.whatsappstatussaver.player.presentation

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import com.breens.whatsappstatussaver.navigation.Video
import com.breens.whatsappstatussaver.player.data.VideoPlayer

@kotlin.OptIn(ExperimentalMaterial3Api::class)
@OptIn(UnstableApi::class)
@Composable
fun PreviewVideoScreen(
    video: Video?,
    viewModel: PreviewVideoViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.setPlayer(uri = video?.videoUri)
    }

    val videoPlayer = viewModel.videoPlayer

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {
                            viewModel.stopVideoPlayer()
                            navigateBack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.Black,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        VideoPlayer(videoPlayer = videoPlayer, modifier = Modifier.padding(paddingValues))
    }
}