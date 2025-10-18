package com.cincinnatiai.ssr_simple

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import com.cincinnatiai.ssr_simple.provider.DefaultDeserializerProvider
import com.cincinnatiai.ssr_simple.provider.DeserializerProvider
import com.cincinnatiai.ssr_simple.ui.RenderNode

object SSRSimpleLibrary {

    var deserializerProvider: DeserializerProvider = DefaultDeserializerProvider()

    @SuppressLint("LogNotTimber")
    @Composable
    fun ShowScreen(json: String, navigate: (String) -> Unit) {
        val root = deserializerProvider.deserializeToNodeModel(json)
        RenderNode(root) { action ->
            when {
                action.startsWith("navigate:") -> navigate(action.removePrefix("navigate:"))
                else -> Log.w("SSRSimpleLibrary", "Unable to handle action: $action")
            }
        }
    }
}