package com.cincinnatiai.serversiderenderer.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.cincinnatiai.serversiderenderer.R

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: Int
)

val bottomNavItems = listOf(
    BottomNavItem("home", Icons.Default.Home, R.string.home),
    BottomNavItem("feed", Icons.Default.Search, R.string.feed),
    BottomNavItem("profile", Icons.Default.Person, R.string.profile),
    BottomNavItem("settings", Icons.Default.Settings, R.string.settings)
)

fun getScreenTitle(route: String): Int {
    return when (route) {
        "home" -> R.string.home
        "feed" -> R.string.feed
        "profile" -> R.string.profile
        "settings" -> R.string.settings
        else -> R.string.app_name
    }
}