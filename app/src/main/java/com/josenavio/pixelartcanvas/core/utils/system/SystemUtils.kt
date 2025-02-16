package com.namenjo.pixmix.core.utils.system

import android.content.Context
import android.content.res.Configuration
import androidx.activity.SystemBarStyle
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb


class SystemUtils {

    companion object {

        // So devices that still use 2 or 3 navigation buttons can have a transparent navigation bar
        fun getNavigationTransparentBar(isSystemInDarkMode: Boolean): SystemBarStyle {

            return if (isSystemInDarkMode) {
                SystemBarStyle.dark(
                    scrim = android.graphics.Color.TRANSPARENT,
                )
            } else {
                SystemBarStyle.light(
//                    scrim = android.graphics.Color.TRANSPARENT,
                    scrim = android.graphics.Color.GRAY,
                    darkScrim = android.graphics.Color.TRANSPARENT
                )
            }
        }


        fun isSystemInDarkMode(context: Context): Boolean {
            return (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        }
    }
}
