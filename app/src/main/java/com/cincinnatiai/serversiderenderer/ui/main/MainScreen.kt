package com.cincinnatiai.serversiderenderer.ui.main

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cincinnatiai.serversiderenderer.R
import com.cincinnatiai.serversiderenderer.actionhandler.CustomAppActionHandler
import com.cincinnatiai.serversiderenderer.navigation.bottomNavItems
import com.cincinnatiai.serversiderenderer.navigation.getScreenTitle
import com.cincinnatiai.serversiderenderer.ui.charts.ChartsScreen
import com.cincinnatiai.serversiderenderer.ui.dashboard.DashboardScreen
import com.cincinnatiai.serversiderenderer.ui.home.HomeScreen
import com.cincinnatiai.serversiderenderer.ui.loading.LoadingScreen
import com.cincinnatiai.serversiderenderer.ui.profile.ProfileScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val currentDestination by navController.currentBackStackEntryAsState()
    val currentRoute = currentDestination?.destination?.route
    val title = stringResource(R.string.app_name)

    LaunchedEffect(Unit) {
        CustomAppActionHandler.navController = navController
    }

    Scaffold(
        modifier = Modifier.background(Color.White),
        topBar = {
            TopAppBar(
                title = {
                    Text(title)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { item ->
                    val label = stringResource(item.label)
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = label
                            )
                        },
                        label = { Text(label) },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route)
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") { ProfileScreen() }
            composable("charts") { ChartsScreen() }
            composable("profile") { ProfileScreen() }
            composable("dashboard") { DashboardScreen() }
            composable("settings") { LoadingScreen() }
        }
    }
}