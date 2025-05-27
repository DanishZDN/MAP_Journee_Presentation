package com.android.example.mobileapp_journeeg9.ui.util

import android.app.Activity
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

enum class NavigationType {
    BOTTOM_NAVIGATION,
    NAVIGATION_RAIL,
    PERMANENT_NAVIGATION_DRAWER
}

enum class ContentType {
    SINGLE_PANE,
    DUAL_PANE
}

/**
 * Remembers and calculates the content type and navigation type for the window size.
 */
@Composable
fun rememberWindowStateUtil(
    windowSizeClass: WindowSizeClass
): WindowStateUtil {
    val configuration = LocalConfiguration.current
    return remember(configuration, windowSizeClass) {
        WindowStateUtil(windowSizeClass)
    }
}

class WindowStateUtil(
    private val windowSizeClass: WindowSizeClass
) {
    val navigationType: NavigationType
        get() = when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> NavigationType.BOTTOM_NAVIGATION
            WindowWidthSizeClass.Medium -> NavigationType.NAVIGATION_RAIL
            WindowWidthSizeClass.Expanded -> NavigationType.PERMANENT_NAVIGATION_DRAWER
            else -> NavigationType.BOTTOM_NAVIGATION
        }

    val contentType: ContentType
        get() = when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> ContentType.SINGLE_PANE
            WindowWidthSizeClass.Medium -> ContentType.DUAL_PANE
            WindowWidthSizeClass.Expanded -> ContentType.DUAL_PANE
            else -> ContentType.SINGLE_PANE
        }

    val shouldShowBottomBar: Boolean
        get() = navigationType == NavigationType.BOTTOM_NAVIGATION

    val shouldShowNavRail: Boolean
        get() = navigationType == NavigationType.NAVIGATION_RAIL

    val shouldShowNavDrawer: Boolean
        get() = navigationType == NavigationType.PERMANENT_NAVIGATION_DRAWER

    val shouldShowTwoPanes: Boolean
        get() = contentType == ContentType.DUAL_PANE
}

data class DevicePosture(
    val isLandscape: Boolean,
    val isFolded: Boolean,
    val hingePosition: Int
)

@Composable
fun rememberDevicePosture(): DevicePosture {
    val configuration = LocalConfiguration.current
    return remember(configuration) {
        DevicePosture(
            isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE,
            isFolded = false, // Add foldable device detection if needed
            hingePosition = 0
        )
    }
}
