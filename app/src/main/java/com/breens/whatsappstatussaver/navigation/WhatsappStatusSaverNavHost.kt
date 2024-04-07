package com.breens.whatsappstatussaver.navigation

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.breens.whatsappstatussaver.onboarding.ImagesOnboardingScreen
import com.breens.whatsappstatussaver.player.domain.Video
import com.breens.whatsappstatussaver.player.domain.VideoType
import com.breens.whatsappstatussaver.player.presentation.PreviewVideoScreen
import com.breens.whatsappstatussaver.statuses.presentation.StatusesScreen
import com.google.gson.Gson

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
            StatusesScreen(
                playVideo = {
                    val video = Video(videoUri = it.toString())
                    val json = Uri.encode(Gson().toJson(video))
                    navHostController.navigate(
                        route = "preview_video/$json",
                    )
                }
            )
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

        composable(
            "preview_video/{video}",
            arguments = listOf(navArgument("video") { type = VideoType() })
        ) { backStackEntry ->
            PreviewVideoScreen(
                video = backStackEntry.arguments?.getParcelable("video"),
                navigateBack = {
                    navHostController.popBackStack()
                }
            )
        }
    }
}