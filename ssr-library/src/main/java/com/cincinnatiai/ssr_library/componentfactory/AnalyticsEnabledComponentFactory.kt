package com.cincinnatiai.ssr_library.componentfactory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import com.cincinnatiai.ssr_library.ComponentFactory
import com.cincinnatiai.ssr_library.analytics.SSRAnalyticsProvider
import com.cincinnatiai.ssr_library.analytics.ServerSideRenderingAnalytics
import com.cincinnatiai.ssr_library.context.ScreenContext
import com.cincinnatiai.ssr_library.context.ScreenContextData
import com.cincinnatiai.ssr_library.model.ActionHandler
import com.cincinnatiai.ssr_library.model.CoilImageLoader
import com.cincinnatiai.ssr_library.model.ComponentNode
import com.cincinnatiai.ssr_library.model.ComponentRenderEvent
import com.cincinnatiai.ssr_library.model.DefaultActionHandler
import com.cincinnatiai.ssr_library.model.ImageLoader
import com.cincinnatiai.ssr_library.model.PerformanceEvent
import timber.log.Timber


val LocalScreenContext = compositionLocalOf<ScreenContextData?> { null }

class AnalyticsEnabledComponentFactory(
    analyticsProvider: SSRAnalyticsProvider,
    private val actionHandler: ActionHandler = DefaultActionHandler(),
    private val imageLoader: ImageLoader = CoilImageLoader(),
    private val baseFactory: ComponentFactory = DefaultComponentFactory(actionHandler, imageLoader),
) : ComponentFactory {

    private val analytics: ServerSideRenderingAnalytics by lazy {
        analyticsProvider.provideAnalytics()
    }

    override suspend fun prepareComponent(node: ComponentNode): ComponentPreparationResult {
        val startTime = System.currentTimeMillis()
        val componentId = node.id ?: "component_${node.hashCode()}"

        return try {
            val result = baseFactory.prepareComponent(node)
            val renderTime = System.currentTimeMillis() - startTime

            analytics.trackPerformance(
                PerformanceEvent(
                    eventType = "component_preparation",
                    screenId = ScreenContext.getCurrentScreenId(),
                    componentId = componentId,
                    value = renderTime.toDouble(),
                    unit = "ms",
                    metadata = mapOf(
                        "component_type" to node.type,
                        "has_children" to (node.children?.isNotEmpty() == true),
                        "children_count" to (node.children?.size ?: 0),
                        "has_properties" to (node.properties?.isNotEmpty() == true),
                        "properties_count" to (node.properties?.size ?: 0)
                    ),
                    sessionId = analytics.getSessionId(),
                    userId = analytics.getUserId()
                )
            )
            Timber.tag("SSR_COMPONENT_PREP")
                .v("Prepared ${node.type}($componentId) in ${renderTime}ms for screen ${ScreenContext.getCurrentScreenId()}")
            result
        } catch (e: Exception) {
            throw e
        }
    }

    override fun createComponent(node: ComponentNode): @Composable (() -> Unit) {
        return {
            val screenContext = LocalScreenContext.current
            val componentId = node.id ?: "component_${node.hashCode()}"
            val startTime = remember { System.currentTimeMillis() }

            LaunchedEffect(node) {
                val renderTime = System.currentTimeMillis() - startTime
                val screenId = screenContext?.screenId ?: ScreenContext.getCurrentScreenId()

                analytics.trackComponentRender(
                    ComponentRenderEvent(
                        componentId = componentId,
                        componentType = node.type,
                        screenId = screenId,
                        renderTime = renderTime,
                        properties = node.properties ?: emptyMap(),
                        userId = analytics.getUserId(),
                        hasChildren = node.children?.isNotEmpty() == true,
                        childrenCount = node.children?.size ?: 0,
                        sessionId = analytics.getSessionId(),
                    )
                )

                Timber.tag("COMPONENT_RENDER")
                    .v("Rendered ${node.type}($componentId) in ${renderTime}ms on screen $screenId")
            }
            val component = baseFactory.createComponent(node)
            component()
        }
    }
}