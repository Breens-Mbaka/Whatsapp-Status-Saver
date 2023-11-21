package com.breens.whatsappstatussaver.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.breens.whatsappstatussaver.onboarding.DownloadingOnboardingScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val downloadingOnboardingScreenViewModel: DownloadingOnboardingScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContent {
            LaunchedEffect(key1 = true) {
                downloadingOnboardingScreenViewModel.getIsOnboardingCompleted()
            }

            val isOnBoardingCompletedState =
                downloadingOnboardingScreenViewModel.isOnBoardingCompletedState

            WhatsappStatusSaverApp(
                isOnBoardingCompleted = isOnBoardingCompletedState.value,
            )
        }
    }

    @Composable
    fun WhatsappStatusSaverApp(isOnBoardingCompleted: Boolean) {
        val navHostController = rememberNavController()

        WhatsappStatusSaverNavHost(
            navHostController = navHostController,
            isOnBoardingCompleted = isOnBoardingCompleted,
        )
    }
}
