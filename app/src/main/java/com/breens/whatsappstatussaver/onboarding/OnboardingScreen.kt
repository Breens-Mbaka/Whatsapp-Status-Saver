package com.breens.whatsappstatussaver.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.breens.whatsappstatussaver.R
import com.breens.whatsappstatussaver.theme.WhatsappStatusSaverTheme

@Composable
fun OnboardingScreen(
    navigateToStatusScreen: () -> Unit,
    downloadingOnboardingScreenViewModel: DownloadingOnboardingScreenViewModel = hiltViewModel(),
) {
    WhatsappStatusSaverTheme {
        OnBoardingScreenContent(
            setIsOnboardingCompleted = {
                downloadingOnboardingScreenViewModel.setIsOnboardingCompleted(it)
            },
            navigateToStatusScreen = navigateToStatusScreen
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoardingScreenContent(
    setIsOnboardingCompleted: (Boolean) -> Unit,
    navigateToStatusScreen: () -> Unit,
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.download_onboarding),
                modifier = Modifier.size(height = 400.dp, width = 300.dp),
                contentDescription = "Download Images and Videos",
            )

            Text(
                text = "Easily save and share Whatsapp Statuses",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = W500
                )
            )

            IconButton(
                onClick = {
                    setIsOnboardingCompleted(true)
                    navigateToStatusScreen()
                },
                modifier = Modifier
                    .size(60.dp)
                    .clip(
                        CircleShape,
                    ),
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}