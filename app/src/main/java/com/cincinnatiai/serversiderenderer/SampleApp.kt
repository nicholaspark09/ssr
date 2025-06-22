package com.cincinnatiai.serversiderenderer

import android.app.Application
import com.cincinnatiai.serversiderenderer.actionhandler.CustomAppActionHandler
import com.cincinnatiai.ssr_library.SSRLogging
import com.cincinnatiai.ssr_library.ServerSideRenderingLibrary
import timber.log.Timber

class SampleApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        // Initialize Library at app onCreate to prevent crashes
        ServerSideRenderingLibrary.initializeWithAnalytics(actionHandler = CustomAppActionHandler)
        SSRLogging.enable()
    }
}