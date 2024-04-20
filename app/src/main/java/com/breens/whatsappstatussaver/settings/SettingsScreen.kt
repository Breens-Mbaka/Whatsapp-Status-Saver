package com.breens.whatsappstatussaver.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Settings", style = MaterialTheme.typography.titleLarge)
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SettingCard(
                    label = "Privacy Policy",
                    icon = Icons.Filled.Policy,
                    onClick = {
                        navigateToPrivacyPolicy(context = context)
                    }
                )
            }
            item {
                SettingCard(
                    label = "Support",
                    icon = Icons.Filled.SupportAgent,
                    onClick = {
                        sendSupportEmail(context = context)
                    }
                )
            }
        }
    }
}

@Composable
fun SettingCard(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W500)
            )

            IconButton(
                onClick = onClick
            ) {
                Icon(imageVector = icon, contentDescription = null)
            }
        }
    }
}

fun navigateToPrivacyPolicy(context: Context) {
    val url =
        "https://doc-hosting.flycricket.io/status-saver-privacy-policy/5be064ae-5023-4c71-8101-8c7afe18880d/privacy"
    val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(urlIntent)
}

fun sendSupportEmail(context: Context) {
    val emailSend = "mbakabreens@gmail.com"
    val emailSubject = "Status Saver Support"

    val intent = Intent(Intent.ACTION_SEND)
    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailSend))
    intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject)
    intent.type = "message/rfc822"

    context.startActivity(Intent.createChooser(intent, "Choose an Email client :"))
}