package com.breens.whatsappstatussaver.statuses.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.unit.dp

@Composable
fun HelpInstructionsDialog(closeDialog: () -> Unit) {
    AlertDialog(
        onDismissRequest = closeDialog,
        confirmButton = { /*TODO*/ },
        shape = MaterialTheme.shapes.medium,
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "1. Open WhatsApp and view the status you want to save.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = W500
                    )
                )
                Text(
                    text = "2. Open Status Saver App",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = W500
                    )
                )
                Text(
                    text = "3. Give permission to this folder path \n\n 'Android > media> com.whatsapp > WhatsApp > Media > .Statuses'",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = W600
                    ),
                )
            }
        }
    )
}