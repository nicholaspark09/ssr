package com.cincinnatiai.ssr_library.model

data class ComponentMetadata(
    val componentCount: Int,
    val maxDepth: Int,
    val hasAsyncComponents: Boolean,
    val estimatedRenderTime: Long
)