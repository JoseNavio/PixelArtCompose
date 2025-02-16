package com.josenavio.pixelartcanvas.core.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.josenavio.pixelartcanvas.ui.screens.main.MainScreen
import com.josenavio.pixelartcanvas.ui.screens.pixel.PixelScreen
import com.josenavio.pixelartcanvas.ui.screens.pixel.PixelViewModel
import com.josenavio.pixelartcanvas.ui.screens.splash.SplashScreen

@Composable
fun NavigationManager(
    scaffoldPadding: PaddingValues,
    pixelViewModel: PixelViewModel,
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = MainNavigation.Graph,
    ) {

        // Splash
        composable<Splash> {
            SplashScreen() {
                navController.navigate(Main)
            }
        }

        // Main
        composable<Main> {
            MainScreen() {
                navController.navigate(MainNavigation.Graph)
            }
        }

        // Main - Navigation
        navigation<MainNavigation.Graph>(
            startDestination = MainNavigation.Pixel
        ) {

            // Pixel
            composable<MainNavigation.Pixel> {

                val pixelState by pixelViewModel.state.collectAsState()

                PixelScreen(
                    pixelState = pixelState,
                    pixelEvent = pixelViewModel::onEvent
                )
            }

        }
    }
}