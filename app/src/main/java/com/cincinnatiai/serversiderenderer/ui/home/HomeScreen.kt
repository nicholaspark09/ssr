package com.cincinnatiai.serversiderenderer.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cincinnatiai.serversiderenderer.ui.error.ErrorScreen
import com.cincinnatiai.serversiderenderer.ui.loading.LoadingScreen
import com.cincinnatiai.ssr_library.ServerSideRenderingLibrary

@Composable
fun HomeScreen() {
    val homeViewModel: HomeViewModel = viewModel<HomeViewModel>()
    val renderer by lazy {
        ServerSideRenderingLibrary.getInstance().serverSideRenderer
    }
    val jsonState = homeViewModel.homeViewState.collectAsState()

    LaunchedEffect(Unit) {
        homeViewModel.start()
    }

    renderer.RenderScreen(jsonState.value, loadingContent = { LoadingScreen() }, errorContent = { message, retry -> ErrorScreen(message, retry)})
}