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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.breens.whatsappstatussaver.R
import com.breens.whatsappstatussaver.ui.theme.PrimaryColor800
import com.breens.whatsappstatussaver.ui.theme.WhatsappStatusSaverTheme

@Composable
fun ImagesOnboardingScreen(
    navigateToImagesScreen: () -> Unit,
    downloadingOnboardingScreenViewModel: DownloadingOnboardingScreenViewModel = hiltViewModel(),
) {
    WhatsappStatusSaverTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                        ),
                    ) {
                        append("Save \n\n")
                    }

                    withStyle(
                        style = SpanStyle(
                            color = Color.Black,
                            fontWeight = FontWeight.Light,
                        ),
                    ) {
                        append("Save images and videos that you've viewed on your Whatsapp Status")
                    }
                },
                textAlign = TextAlign.Center,
            )

            IconButton(
                onClick = {
                    downloadingOnboardingScreenViewModel.setIsOnboardingCompleted(
                        isOnBoardingCompleted = true,
                    )
                    navigateToImagesScreen()
                },
                modifier = Modifier
                    .size(60.dp)
                    .clip(
                        CircleShape,
                    ),
                colors = IconButtonDefaults.iconButtonColors(containerColor = PrimaryColor800),
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                )
            }
        }
    }
}
