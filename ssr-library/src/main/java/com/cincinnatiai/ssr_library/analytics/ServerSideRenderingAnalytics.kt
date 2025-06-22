package com.cincinnatiai.ssr_library.analytics

import com.cincinnatiai.ssr_library.model.ComponentErrorEvent
import com.cincinnatiai.ssr_library.model.ComponentRenderEvent
import com.cincinnatiai.ssr_library.model.CustomEvent
import com.cincinnatiai.ssr_library.model.PerformanceEvent
import com.cincinnatiai.ssr_library.model.ScreenErrorEvent
import com.cincinnatiai.ssr_library.model.ScreenRenderEvent
import com.cincinnatiai.ssr_library.model.UserActionEvent


interface ServerSideRenderingAnalytics {
    fun getUserId(): String? = null
    fun getSessionId(): String
    fun trackScreenRender(event: ScreenRenderEvent)
    fun trackComponentRender(event: ComponentRenderEvent)
    fun trackUserAction(event: UserActionEvent)
    fun trackPerformance(event: PerformanceEvent)
    fun trackError(event: ComponentErrorEvent)
    fun trackScreenError(event: ScreenErrorEvent)
    fun trackCustomEvent(event: CustomEvent)
    fun flush()
    fun setUserId(userId: String)
    fun setUserProperty(key: String, value: Any)
}

interface SSRAnalyticsProvider {
    fun provideAnalytics(): ServerSideRenderingAnalytics
}