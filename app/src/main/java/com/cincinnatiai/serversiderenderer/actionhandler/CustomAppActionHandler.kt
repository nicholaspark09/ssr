package com.cincinnatiai.serversiderenderer.actionhandler

import android.util.Log
import androidx.navigation.NavHostController
import com.cincinnatiai.ssr_library.model.ActionConfig
import com.cincinnatiai.ssr_library.model.ActionHandler

object CustomAppActionHandler : ActionHandler {

    var navController: NavHostController? = null

    override fun handleAction(action: ActionConfig) {
        when (action.type) {
            "navigate" -> {
                when (action.destination) {
                    "profile" -> navController?.navigate(action.destination ?: "")
                }
            }

            else -> Log.w("ACTION_HANDLER", "Unknown action type: ${action.type}")
        }
    }
}