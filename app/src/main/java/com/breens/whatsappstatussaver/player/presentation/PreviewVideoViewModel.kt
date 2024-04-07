package com.breens.whatsappstatussaver.player.presentation

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class PreviewVideoViewModel @Inject constructor(
    val videoPlayer: Player,
) : ViewModel() {
    fun setPlayer(uri: String?) {
        viewModelScope.launch {
            if (uri != null) {
                videoPlayer.setMediaItem(MediaItem.fromUri(uri.toUri()))
            }
        }
    }

    fun stopVideoPlayer() {
        videoPlayer.stop()
    }

    override fun onCleared() {
        super.onCleared()
        videoPlayer.stop()
    }
}