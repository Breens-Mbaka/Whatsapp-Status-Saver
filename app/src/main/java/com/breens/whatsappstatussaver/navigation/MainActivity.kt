package com.breens.whatsappstatussaver.navigation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.breens.whatsappstatussaver.onboarding.DownloadingOnboardingScreenViewModel
import com.breens.whatsappstatussaver.theme.WhatsappStatusSaverTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val downloadingOnboardingScreenViewModel: DownloadingOnboardingScreenViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            LaunchedEffect(key1 = true) {
                downloadingOnboardingScreenViewModel.getIsOnboardingCompleted()
            }

            val isOnBoardingCompletedState =
                downloadingOnboardingScreenViewModel.isOnBoardingCompletedState

            WhatsappStatusSaverTheme {
                WhatsappStatusSaverApp(
                    isOnBoardingCompleted = isOnBoardingCompletedState.value,
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    fun WhatsappStatusSaverApp(isOnBoardingCompleted: Boolean) {
        val navHostController = rememberNavController()

        WhatsappStatusSaverNavHost(
            navHostController = navHostController,
            startDestination = if (isOnBoardingCompleted) "images" else "onboarding_screen",
        )
    }
}
