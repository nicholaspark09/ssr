package com.cincinnatiai.serversiderenderer.constants

val loadingScreen = """
    {
      "screen": {
        "id": "loading_screen",
        "title": "Loading",
        "layout": {
          "type": "progress_indicator",
          "id": "loading_spinner",
          "modifier": {
            "fillMaxSize": true
          }
        }
      }
    }
""".trimIndent()