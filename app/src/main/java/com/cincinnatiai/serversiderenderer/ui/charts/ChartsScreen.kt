package com.cincinnatiai.serversiderenderer.ui.charts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cincinnatiai.serversiderenderer.ui.error.ErrorScreen
import com.cincinnatiai.serversiderenderer.ui.loading.LoadingScreen
import com.cincinnatiai.ssr_library.ServerSideRenderingLibrary

@Composable
fun ChartsScreen() {
    val chartsViewModel: ChartsViewModel = viewModel<ChartsViewModel>()
    val renderer by lazy {
        ServerSideRenderingLibrary.getInstance().serverSideRenderer
    }
    val jsonState = chartsViewModel.chartsViewState.collectAsState()

    LaunchedEffect(Unit) {
        chartsViewModel.start()
    }

    renderer.RenderScreen(jsonState.value, loadingContent = { LoadingScreen() }, errorContent = { message, retry -> ErrorScreen(message, retry)})
}