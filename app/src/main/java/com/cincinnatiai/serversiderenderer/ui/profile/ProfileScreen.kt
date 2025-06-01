package com.cincinnatiai.serversiderenderer.ui.profile

import androidx.compose.runtime.Composable
import com.cincinnatiai.serversiderenderer.constants.profileScreenJson
import com.cincinnatiai.serversiderenderer.ui.error.ErrorScreen
import com.cincinnatiai.serversiderenderer.ui.loading.LoadingScreen
import com.cincinnatiai.ssr_library.ServerSideRenderingLibrary

@Composable
fun ProfileScreen() {
    val renderer by lazy {
        ServerSideRenderingLibrary.getInstance().serverSideRenderer
    }

    renderer.RenderScreen(profileScreenJson, loadingContent = { LoadingScreen() }, errorContent = { message, retry -> ErrorScreen(message, retry)})
}