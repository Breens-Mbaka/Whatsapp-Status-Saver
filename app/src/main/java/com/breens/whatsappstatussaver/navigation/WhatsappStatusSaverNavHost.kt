package com.breens.whatsappstatussaver.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.breens.whatsappstatussaver.statuses.presentation.StatusesScreen
import com.breens.whatsappstatussaver.onboarding.ImagesOnboardingScreen

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun WhatsappStatusSaverNavHost(
    navHostController: NavHostController,
    startDestination: String,
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
    ) {
        composable(
            route = "images",
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700),
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700),
                )
            },
        ) {
            StatusesScreen()
        }

        composable(
            route = "download_onboarding_screen",
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700),
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700),
                )
            },
        ) {
            ImagesOnboardingScreen(
                navigateToImagesScreen = {
                    navHostController.navigate(
                        route = "images",
                    )
                },
            )
        }
    }
}
