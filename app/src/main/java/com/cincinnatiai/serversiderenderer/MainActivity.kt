package com.cincinnatiai.serversiderenderer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cincinnatiai.serversiderenderer.constants.homeScreenJson
import com.cincinnatiai.serversiderenderer.constants.scrollableCards
import com.cincinnatiai.serversiderenderer.constants.variousCards
import com.cincinnatiai.serversiderenderer.ui.main.MainScreen
import com.cincinnatiai.serversiderenderer.ui.theme.ServerSideRendererTheme
import com.cincinnatiai.ssr_library.ServerSideRenderingLibrary

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ServerSideRenderingLibrary.initialize()
        enableEdgeToEdge()
        setContent {
            ServerSideRendererTheme {
                MainScreen()
            }
        }
    }
}
