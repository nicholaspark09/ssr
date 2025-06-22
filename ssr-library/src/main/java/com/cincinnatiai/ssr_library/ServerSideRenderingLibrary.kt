package com.cincinnatiai.ssr_library

import androidx.compose.runtime.Composable
import com.cincinnatiai.ssr_library.analytics.DefaultServerSideRenderingAnalyticsImpl
import com.cincinnatiai.ssr_library.analytics.SSRAnalyticsProvider
import com.cincinnatiai.ssr_library.analytics.ServerSideRenderingAnalytics
import com.cincinnatiai.ssr_library.componentfactory.AnalyticsEnabledComponentFactory
import com.cincinnatiai.ssr_library.componentfactory.DefaultComponentFactory
import com.cincinnatiai.ssr_library.model.ActionHandler
import com.cincinnatiai.ssr_library.model.CoilImageLoader
import com.cincinnatiai.ssr_library.model.DefaultActionHandler
import com.cincinnatiai.ssr_library.model.ImageLoader

class ServerSideRenderingLibrary private constructor(
    componentFactory: ComponentFactory, imageLoader: ImageLoader,
) {

    val serverSideRenderer: ServerSideRenderer by lazy {
        ServerSideRendererImpl(imageLoader = imageLoader, componentFactory = componentFactory)
    }

    /**
     *  To use this, please look at the `ChartsScreen` in the demo app
     */
    fun createAnalyticsEnabledRenderer(): ServerSideRenderer = serverSideRenderer

    companion object {
        @Volatile
        var INSTANCE: ServerSideRenderingLibrary? = null

        fun initialize(
            actionHandler: ActionHandler = DefaultActionHandler(),
            componentFactory: ComponentFactory = DefaultComponentFactory(actionHandler = actionHandler),
            imageLoader: ImageLoader = CoilImageLoader(),
        ) {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = ServerSideRenderingLibrary(componentFactory, imageLoader)
                    }
                }
            }
        }

        fun initializeWithAnalytics(
            analyticsProvider: SSRAnalyticsProvider? = null,
            actionHandler: ActionHandler = DefaultActionHandler(),
            imageLoader: ImageLoader = CoilImageLoader(),
        ) {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = ServerSideRenderingLibrary(
                            AnalyticsEnabledComponentFactory(
                                analyticsProvider ?: object : SSRAnalyticsProvider {

                                    override fun provideAnalytics(): ServerSideRenderingAnalytics =
                                        DefaultServerSideRenderingAnalyticsImpl()
                                },
                                actionHandler,
                                imageLoader
                            ), imageLoader
                        )
                    }
                }
            }
        }

        fun getInstance() =
            INSTANCE ?: throw IllegalAccessException("Must call initialize on library before using")
    }
}