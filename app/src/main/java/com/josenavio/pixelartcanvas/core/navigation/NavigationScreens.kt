package com.josenavio.pixelartcanvas.core.navigation

import kotlinx.serialization.Serializable

@Serializable
data object Splash

@Serializable
data object Main

sealed interface MainNavigation {

    @Serializable
    data object Graph : MainNavigation

    @Serializable
    data object Pixel : MainNavigation

    @Serializable
    data object Menu : MainNavigation

    @Serializable
    data object Canvas : MainNavigation
}


