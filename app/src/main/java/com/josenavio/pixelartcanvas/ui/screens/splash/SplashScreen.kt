package com.josenavio.pixelartcanvas.ui.screens.splash
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SplashScreen(navigate: () -> Unit) {
    Scaffold() {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            Text("Splash")
            Button(onClick = { navigate() }) { Text("Go...") }
        }
    }
}