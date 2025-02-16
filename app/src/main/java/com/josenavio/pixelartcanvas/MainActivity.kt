package com.josenavio.pixelartcanvas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.josenavio.pixelartcanvas.core.navigation.NavigationManager
import com.josenavio.pixelartcanvas.ui.screens.pixel.PixelViewModel
import com.josenavio.pixelartcanvas.ui.theme.PixelArtCanvasTheme
import com.namenjo.pixmix.core.utils.system.SystemUtils

class MainActivity : ComponentActivity() {

    private val pixelViewModel by viewModels<PixelViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isDarkMode = SystemUtils.isSystemInDarkMode(this)
        // Transparent navigation bar
        enableEdgeToEdge(navigationBarStyle = SystemUtils.getNavigationTransparentBar(isDarkMode))

        setContent {

            PixelArtCanvasTheme{

                MainNavigationComponent(
                    pixelViewModel = pixelViewModel
                )

            }
        }
    }
}

@Composable
private fun MainNavigationComponent(
    pixelViewModel: PixelViewModel,
) {

    Scaffold(
        contentWindowInsets = WindowInsets.navigationBars
    ) { innerPadding ->

        // Navigator
        NavigationManager(
            scaffoldPadding = innerPadding,
            pixelViewModel = pixelViewModel
        )
    }
}

