package com.breens.whatsappstatussaver.navigation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.breens.whatsappstatussaver.onboarding.DownloadingOnboardingScreenViewModel
import com.breens.whatsappstatussaver.theme.WhatsappStatusSaverTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val downloadingOnboardingScreenViewModel: DownloadingOnboardingScreenViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            LaunchedEffect(key1 = true) {
                downloadingOnboardingScreenViewModel.getIsOnboardingCompleted()
            }

            val navHostController = rememberNavController()
            val items = listOf(Screen.StatusesScreen, Screen.SettingsScreen)
            val isOnBoardingCompleted =
                downloadingOnboardingScreenViewModel.isOnBoardingCompletedState.collectAsState().value

            WhatsappStatusSaverTheme {
                val navBackStackEntry by navHostController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                Scaffold(
                    bottomBar = {
                        if (currentDestination?.hierarchy?.any { it.route == Screen.OnboardingScreen.route } == true) {
                            return@Scaffold
                        }

                        BottomAppBar(
                            windowInsets = BottomAppBarDefaults.windowInsets
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                items.forEach { screen ->
                                    IconButton(
                                        modifier = Modifier.weight(1f),
                                        onClick = {
                                            navHostController.navigate(screen.route) {
                                                popUpTo(navHostController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Icon(
                                                imageVector = when (screen) {
                                                    Screen.StatusesScreen -> Icons.Filled.Home
                                                    Screen.SettingsScreen -> Icons.Filled.Settings
                                                    else -> Icons.Filled.Home
                                                },
                                                contentDescription = null,
                                                modifier = Modifier.size(18.dp),
                                                tint = if (currentDestination?.hierarchy?.any { it.route == screen.route } == true) {
                                                    MaterialTheme.colorScheme.primary
                                                } else {
                                                    MaterialTheme.colorScheme.onBackground
                                                }
                                            )
                                            Text(
                                                text = if (screen.route == Screen.StatusesScreen.route) "Statuses" else "Settings",
                                                fontSize = 14.sp,
                                                color = if (currentDestination?.hierarchy?.any { it.route == screen.route } == true) {
                                                    MaterialTheme.colorScheme.primary
                                                } else {
                                                    MaterialTheme.colorScheme.onBackground
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                ) { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        WhatsappStatusSaverApp(
                            isOnBoardingCompleted = isOnBoardingCompleted,
                            navHostController = navHostController
                        )
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    fun WhatsappStatusSaverApp(
        isOnBoardingCompleted: Boolean,
        navHostController: NavHostController,
    ) {
        WhatsappStatusSaverNavHost(
            navHostController = navHostController,
            startDestination = if (isOnBoardingCompleted) Screen.StatusesScreen.route else Screen.OnboardingScreen.route,
        )
    }
}
