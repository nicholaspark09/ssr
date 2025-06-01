package com.cincinnatiai.ssr_library.model

interface ActionHandler {
    fun handleAction(action: ActionConfig)
}

class DefaultActionHandler : ActionHandler {
    override fun handleAction(action: ActionConfig) {
        when (action.type) {
            "navigation" -> {
                // TODO Implement navigation logic here
                println("Navigating to: ${action.destination}")
                action.params?.let { params ->
                    println("With params: $params")
                }
            }
            "api_call" -> {
                // TODO Implement API call logic
                println("Making API call")
            }
        }
    }
}