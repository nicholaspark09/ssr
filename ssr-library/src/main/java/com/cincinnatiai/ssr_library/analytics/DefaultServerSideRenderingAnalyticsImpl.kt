package com.cincinnatiai.ssr_library.analytics

import com.cincinnatiai.ssr_library.model.AnalyticsConfig
import com.cincinnatiai.ssr_library.model.AnalyticsEvent
import com.cincinnatiai.ssr_library.model.AnalyticsEvent.ComponentAction
import com.cincinnatiai.ssr_library.model.ComponentActionEvent
import com.cincinnatiai.ssr_library.model.ComponentErrorEvent
import com.cincinnatiai.ssr_library.model.ComponentRenderEvent
import com.cincinnatiai.ssr_library.model.CustomEvent
import com.cincinnatiai.ssr_library.model.ErrorEvent
import com.cincinnatiai.ssr_library.model.PerformanceEvent
import com.cincinnatiai.ssr_library.model.ScreenErrorEvent
import com.cincinnatiai.ssr_library.model.ScreenRenderEvent
import com.cincinnatiai.ssr_library.model.UserActionEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class DefaultServerSideRenderingAnalyticsImpl(
    private val config: AnalyticsConfig = AnalyticsConfig(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ServerSideRenderingAnalytics {

    private val events = mutableListOf<AnalyticsEvent>()

    @Volatile
    private var userId: String? = null
    private val sessionId = UUID.randomUUID().toString()
    private val screenRenderCounts = mutableMapOf<String, Int>()
    private val screenRenderTimes = mutableMapOf<String, MutableList<Long>>()
    private val screenErrors = ConcurrentHashMap<String, Int>()
    private val componentCounts = mutableMapOf<String, Int>()

    @Volatile
    private var currentScreenId: String? = null

    private val analyticsScope = CoroutineScope(dispatcher + SupervisorJob())

    override fun getSessionId(): String = sessionId

    override fun trackScreenRender(event: ScreenRenderEvent) {
        val enhancedEvent = event.copy(
            userId = userId ?: event.userId,
            sessionId = sessionId
        )
        screenRenderCounts[event.screenId] = (screenRenderCounts[event.screenId] ?: 0) + 1
        screenRenderTimes.computeIfAbsent(event.screenId) { mutableListOf() }.add(event.totalRenderTime)
        currentScreenId = event.screenId
        Timber.tag("SSR_SCREEN_ANALYTICS").d("""
            Screen Rendered:
            - ID: ${enhancedEvent.screenId}
            - Title: ${enhancedEvent.screenTitle}
            - Render Time: ${enhancedEvent.totalRenderTime}ms
            - Components: ${enhancedEvent.componentCount}
            - Charts: ${enhancedEvent.chartCount}
            - Complexity: ${enhancedEvent.complexity}
            - Cache Hit: ${enhancedEvent.cacheHit}
            - Session: ${enhancedEvent.sessionId}
            - User: ${enhancedEvent.userId ?: "anonymous"}
        """.trimIndent())

        addEvent(AnalyticsEvent.ScreenRender(enhancedEvent))
    }

    override fun trackComponentRender(event: ComponentRenderEvent) {
        val enhancedEvent = event.copy(
            userId = userId ?: event.userId,
            sessionId = sessionId,
            screenId = event.screenId.ifEmpty { currentScreenId ?: "unknown" }
        )

        componentCounts[event.componentType] = (componentCounts[event.componentType] ?: 0) + 1

        Timber.tag("SSR_COMPONENT_ANALYTICS").v("Component: ${event.componentType}(${event.componentId}) on ${enhancedEvent.screenId} [Session: $sessionId]")

        addEvent(AnalyticsEvent.ComponentRender(enhancedEvent))
    }

    override fun trackUserAction(event: UserActionEvent) {
        val componentActionEvent = ComponentActionEvent(
            componentId = event.componentId ?: "",
            componentType = event.componentType,
            screenId = event.screenId.ifEmpty { currentScreenId ?: "unknown" },
            actionType = event.actionType,
            actionData = event.actionData,
            userId = userId,
            sessionId = sessionId,
            timestamp = event.timestamp
        )
        addEvent(ComponentAction(componentActionEvent))
        Timber.tag("SSR_COMPONENT_ANALYTICS").d("Action: ${event.actionType} on ${event.componentType}(${event.componentId}) in ${componentActionEvent.screenId} [Session: $sessionId]")
    }

    override fun trackPerformance(event: PerformanceEvent) {
        val enhancedEvent = event.copy(
            userId = userId ?: event.userId,
            sessionId = sessionId,
            screenId = event.screenId.ifEmpty { currentScreenId ?: "unknown" }
        )

        Timber.tag("SSR_PERF_ANALYTICS").d("Performance: ${event.value} ${event.unit} (${enhancedEvent.screenId}) [Session: $sessionId]")
        addEvent(AnalyticsEvent.Performance(enhancedEvent))
    }

    override fun trackError(event: ComponentErrorEvent) {
        val enhancedEvent = event.copy(
            userId = userId ?: event.userId,
            sessionId = sessionId
        )
        Timber.tag("COMPONENT_ANALYTICS").e("Component Error: ${event.componentType}(${event.componentId}) - ${event.errorType}: ${event.errorMessage} [Session: $sessionId]")
        addEvent(AnalyticsEvent.ComponentError(enhancedEvent))
    }

    override fun trackScreenError(event: ScreenErrorEvent) {
        val enhancedEvent = event.copy(
            userId = userId ?: event.userId,
            sessionId = sessionId
        )
        screenErrors[event.screenId] = (screenErrors[event.screenId] ?: 0) + 1
        Timber.tag("SSR_SCREEN_ANALYTICS").e("Screen Error: ${event.screenId} - ${event.errorType}: ${event.errorMessage} [Session: $sessionId]")
        addEvent(AnalyticsEvent.ScreenError(enhancedEvent))
    }

    override fun trackCustomEvent(event: CustomEvent) {
        val enhancedEvent = event.copy(
            userId = userId ?: event.userId,
            sessionId = sessionId,
            screenId = event.screenId?.ifEmpty { currentScreenId ?: "unknown" }
        )
        Timber.tag("SSR_CUSTOM_ANALYTICS").d("Custom: ${event.eventName} on ${enhancedEvent.screenId} [Session: $sessionId]")
        addEvent(AnalyticsEvent.Custom(enhancedEvent))
    }

    override fun flush() {
        analyticsScope.launch {
            val eventsToFlush = synchronized(events) {
                events.toList().also { events.clear() }
            }
            config.eventProcessor?.processEvents(eventsToFlush)
            Timber.tag("SSR_ANALYTICS").d("Flushed ${eventsToFlush.size} events for session $sessionId")
        }
    }

    override fun setUserId(userId: String) {
        synchronized(this) {
            this.userId = userId
        }
    }

    override fun setUserProperty(key: String, value: Any) {
        Timber.tag("SSR_USER_ANALYTICS").d("""
            User Properties
            - Key: $key
            - Value: $value
        """.trimIndent())
    }

    private fun addEvent(event: AnalyticsEvent) {
        synchronized(events) {
            events.add(event)
            if (events.size >= config.batchSize) {
                flush()
            }
        }
    }
}