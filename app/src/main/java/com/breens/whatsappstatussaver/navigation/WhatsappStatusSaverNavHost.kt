package com.breens.whatsappstatussaver.navigation

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.breens.whatsappstatussaver.onboarding.OnboardingScreen
import com.breens.whatsappstatussaver.player.domain.Video
import com.breens.whatsappstatussaver.player.domain.VideoType
import com.breens.whatsappstatussaver.player.presentation.PreviewVideoScreen
import com.breens.whatsappstatussaver.settings.SettingsScreen
import com.breens.whatsappstatussaver.statuses.presentation.StatusesScreen
import com.breens.whatsappstatussaver.statuses.presentation.StatusesViewModel
import com.google.gson.Gson

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun WhatsappStatusSaverNavHost(
    navHostController: NavHostController,
    startDestination: String,
    statusesViewModel: StatusesViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = Unit) {
        statusesViewModel.getSavedUri()
    }

    val savedUri = statusesViewModel.statusesScreenUiState.collectAsState().value.uri

    NavHost(
        navController = navHostController,
        startDestination = startDestination,
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
        composable(route = Screen.StatusesScreen.route) {
            StatusesScreen(
                viewModel = statusesViewModel,
                savedUri = savedUri,
                playVideo = {
                    val video = Video(videoUri = it.toString())
                    val json = Uri.encode(Gson().toJson(video))
                    navHostController.navigate(
                        route = "preview_video/$json",
                    )
                }
            )
        }

        composable(route = Screen.OnboardingScreen.route) {
            OnboardingScreen(
                navigateToStatusScreen = {
                    navHostController.navigate(
                        route = Screen.StatusesScreen.route,
                    )
                },
            )
        }

        composable(
            route = Screen.PreviewVideoScreen.route,
            arguments = listOf(navArgument("video") { type = VideoType() })
        ) { backStackEntry ->
            PreviewVideoScreen(
                video = backStackEntry.arguments?.getParcelable("video"),
                navigateBack = {
                    navHostController.popBackStack()
                }
            )
        }

        composable(route = Screen.SettingsScreen.route) {
            SettingsScreen(
                navigateBack = {
                    navHostController.popBackStack()
                }
            )
        }
    }
}