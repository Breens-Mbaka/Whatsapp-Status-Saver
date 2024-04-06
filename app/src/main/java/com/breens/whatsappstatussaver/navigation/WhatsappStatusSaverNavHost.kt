package com.breens.whatsappstatussaver.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.breens.whatsappstatussaver.statuses.presentation.StatusesScreen
import com.breens.whatsappstatussaver.onboarding.ImagesOnboardingScreen

@Composable
fun WhatsappStatusSaverNavHost(
    navHostController: NavHostController,
    isOnBoardingCompleted: Boolean,
) {
    NavHost(
        navController = navHostController,
        startDestination = if (isOnBoardingCompleted) "images" else "download_onboarding_screen",
    ) {
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
    }
}
