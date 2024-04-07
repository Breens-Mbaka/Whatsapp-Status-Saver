package com.breens.whatsappstatussaver.statuses.presentation.components

import android.net.Uri
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage

@Composable
fun FulImageDialog(imageUri: Uri?, closeDialog: () -> Unit) {
    Dialog(onDismissRequest = closeDialog) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(MaterialTheme.shapes.medium),
            model = imageUri,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
    }
}