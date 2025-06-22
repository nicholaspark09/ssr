package com.cincinnatiai.ssr_library.context

import android.util.Log

data class ScreenContextData(
    val screenId: String,
    val screenTitle: String,
    val screenType: String? = null
)

object ScreenContext {
    private var currentScreenId: String? = null
    private var currentScreenTitle: String? = null

    fun setCurrentScreen(screenId: String, screenTitle: String) {
        currentScreenId = screenId
        currentScreenTitle = screenTitle
        Log.d("SSR_CONTEXT", "Screen context set: $screenId ($screenTitle)")
    }

    fun getCurrentScreenId(): String = currentScreenId ?: "unknown_screen"
    fun getCurrentScreenTitle(): String = currentScreenTitle ?: "Unknown Screen"

    fun clear() {
        currentScreenId = null
        currentScreenTitle = null
    }
}