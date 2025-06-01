package com.cincinnatiai.ssr_library.model

sealed class ComponentRenderState {
    data object Loading : ComponentRenderState()
    data class Success(val composableTree: ComposableTree): ComponentRenderState()
    data class Error(val message: String, val throwable: Throwable? = null) : ComponentRenderState()
}