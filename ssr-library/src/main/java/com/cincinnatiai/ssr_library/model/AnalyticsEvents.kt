package com.cincinnatiai.ssr_library.model

enum class ScreenComplexity {
    LOW, MEDIUM, HIGH, UNKNOWN
}

sealed class AnalyticsEvent {
    data class ScreenRender(val event: ScreenRenderEvent) : AnalyticsEvent()
    data class ScreenNavigation(val event: ScreenNavigationEvent) : AnalyticsEvent()
    data class ScreenError(val event: ScreenErrorEvent) : AnalyticsEvent()
    data class ComponentRender(val event: ComponentRenderEvent) : AnalyticsEvent()
    data class ComponentAction(val event: ComponentActionEvent) : AnalyticsEvent()
    data class ComponentError(val event: ComponentErrorEvent) : AnalyticsEvent() // Added this
    data class Performance(val event: PerformanceEvent) : AnalyticsEvent()
    data class Custom(val event: CustomEvent) : AnalyticsEvent()
}

data class AnalyticsConfig(
    val enableLogging: Boolean = true,
    val enableComponentTracking: Boolean = true,
    val enablePerformanceTracking: Boolean = true,
    val batchSize: Int = 50,
    val eventProcessor: AnalyticsEventProcessor? = null
)

data class SessionAnalytics(
    val sessionId: String,
    val userId: String?,
    val events: List<AnalyticsEvent>,
    val userProperties: Map<String, Any>
)

interface AnalyticsEventProcessor {
    suspend fun processEvents(events: List<AnalyticsEvent>)
}

data class ComponentRenderEvent(
    val componentType: String,
    val componentId: String?,
    val screenId: String,
    val renderTime: Long,
    val properties: Map<String, Any>,
    val hasChildren: Boolean,
    val childrenCount: Int,
    val userId: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val sessionId: String
)

data class ScreenRenderEvent(
    val screenId: String,
    val screenTitle: String,
    val renderTime: Long,
    val componentCount: Int,
    val chartCount: Int,
    val complexity: ScreenComplexity,
    val cacheHit: Boolean,
    val userId: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val sessionId: String,
    val totalRenderTime: Long,
    val screenStructure: String? = null // JSON structure
)

data class UserActionEvent(
    val actionType: String, // "click", "scroll", "swipe", etc.
    val componentType: String,
    val componentId: String?,
    val screenId: String,
    val actionData: Map<String, Any>? = null,
    val userId: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val sessionId: String
)

data class PerformanceEvent(
    val componentId: String,
    val eventType: String, // "composition_time", "cache_performance", "memory_usage"
    val screenId: String,
    val value: Double,
    val unit: String, // "ms", "mb", "count"
    val metadata: Map<String, Any>? = null,
    val userId: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val sessionId: String
)

data class ErrorEvent(
    val errorType: String,
    val errorMessage: String,
    val screenId: String?,
    val componentType: String?,
    val stackTrace: String?,
    val userId: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val sessionId: String
)

data class CustomEvent(
    val eventName: String,
    val properties: Map<String, Any>,
    val screenId: String?,
    val userId: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val sessionId: String
)

data class ScreenNavigationEvent(
    val fromScreenId: String?,
    val toScreenId: String,
    val navigationType: String, // "push", "pop", "replace", "deep_link"
    val navigationTime: Long,
    val userId: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val sessionId: String
)

data class ScreenErrorEvent(
    val screenId: String,
    val errorType: String, // "render_error", "navigation_error", "data_error"
    val errorMessage: String,
    val affectedComponents: List<String> = emptyList(),
    val stackTrace: String?,
    val userId: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val sessionId: String
)

data class ComponentActionEvent(
    val componentId: String,
    val componentType: String,
    val screenId: String,
    val actionType: String, // "click", "scroll", "swipe", "long_press"
    val actionData: Map<String, Any>? = null,
    val userId: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val sessionId: String
)

data class ComponentErrorEvent(
    val componentId: String,
    val componentType: String,
    val screenId: String,
    val errorType: String,
    val errorMessage: String,
    val stackTrace: String?,
    val userId: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val sessionId: String
)
