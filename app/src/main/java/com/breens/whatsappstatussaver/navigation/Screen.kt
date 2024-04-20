package com.breens.whatsappstatussaver.navigation

sealed class Screen(val route: String) {
    object StatusesScreen : Screen("statuses_screen")
    object OnboardingScreen : Screen("onboarding_screen")
    object PreviewVideoScreen : Screen("preview_video/{video}")
    object SettingsScreen : Screen("settings_screen")
}