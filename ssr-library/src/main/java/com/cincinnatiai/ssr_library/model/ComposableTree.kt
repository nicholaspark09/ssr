package com.cincinnatiai.ssr_library.model

import androidx.compose.runtime.Composable

data class ComposableTree(
    val rootComponent: @Composable () -> Unit,
    val metadata: ComponentMetadata
)