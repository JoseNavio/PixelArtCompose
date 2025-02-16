package com.josenavio.pixelartcanvas.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun MainScreen(
    navigate: () -> Unit
) {

    Scaffold(
        contentWindowInsets = WindowInsets.navigationBars
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)

        ) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Red)) {

            }
        }



    }
}