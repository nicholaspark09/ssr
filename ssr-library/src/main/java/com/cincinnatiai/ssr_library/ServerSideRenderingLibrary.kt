package com.cincinnatiai.ssr_library

import com.cincinnatiai.ssr_library.componentfactory.DefaultComponentFactory
import com.cincinnatiai.ssr_library.model.ActionHandler
import com.cincinnatiai.ssr_library.model.CoilImageLoader
import com.cincinnatiai.ssr_library.model.DefaultActionHandler
import com.cincinnatiai.ssr_library.model.ImageLoader

class ServerSideRenderingLibrary private constructor(
    componentFactory: ComponentFactory, imageLoader: ImageLoader
) {

    val serverSideRenderer: ServerSideRenderer by lazy {
        ServerSideRendererImpl(imageLoader = imageLoader, componentFactory = componentFactory)
    }

    companion object {
        @Volatile
        var INSTANCE: ServerSideRenderingLibrary? = null

        fun initialize(
            actionHandler: ActionHandler = DefaultActionHandler(),
            componentFactory: ComponentFactory = DefaultComponentFactory(actionHandler = actionHandler),
            imageLoader: ImageLoader = CoilImageLoader()
        ) {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = ServerSideRenderingLibrary(componentFactory, imageLoader)
                    }
                }
            }
        }

        fun getInstance() = INSTANCE ?: throw IllegalAccessException("Must call initialize on library before using")
    }
}