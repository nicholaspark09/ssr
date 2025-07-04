package com.cincinnatiai.ssr_library.model

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable

interface ActionHandler {
    fun handleAction(action: ActionConfig)
}

sealed class ComponentRenderState {
    data object Loading : ComponentRenderState()
    data class Success(val composableTree: ComposableTree): ComponentRenderState()
    data class Error(val message: String, val throwable: Throwable? = null) : ComponentRenderState()
}

data class ComponentMetadata(
    val componentCount: Int,
    val maxDepth: Int,
    val hasAsyncComponents: Boolean,
    val estimatedRenderTime: Long
)

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

data class ComposableTree(
    val rootComponent: @Composable () -> Unit,
    val metadata: ComponentMetadata
)

data class ScreenLayout(
    val id: String,
    val title: String,
    val layout: ComponentNode
)

data class ModifierConfig(
    val padding: Int? = null,
    val paddingStart: Int? = null,
    val paddingTop: Int? = null,
    val paddingEnd: Int? = null,
    val paddingBottom: Int? = null,
    val paddingHorizontal: Int? = null,
    val paddingVertical: Int? = null,
    val fillMaxSize: Boolean? = null,
    val fillMaxWidth: Boolean? = null,
    val width: Int? = null,
    val height: Int? = null,
    val weight: Float? = null,
    val gradient: GradientConfig? = null
)

data class GradientConfig(
    val type: String, // Only possible values are "linear", "radial", "sweep" (TODO July 4, 2025)
    val colors: List<String>,
    val angle: Float? = null, // (0-360 degrees)
    val startX: Float? = null,
    val startY: Float? = null,
    val endX: Float? = null,
    val endY: Float? = null,
    val center: Pair<Float, Float>? = null, // For radial gradients
    val radius: Float? = null
)

data class ActionConfig(
    val type: String,
    val destination: String? = null,
    val params: Map<String, String>? = null
)

data class ThemeConfig(
    val primaryColor: String,
    val secondaryColor: String,
    val backgroundColor: String,
    val textColor: String
)

data class DataConfig(
    val apiEndpoints: Map<String, String>? = null,
    val staticData: Map<String, Any>? = null
)

data class DataSource(
    val type: String, // "api", "static", "database"
    val url: String? = null,
    val method: String? = null,
    val headers: Map<String, String>? = null,
    val items: List<Map<String, Any>>? = null,
    val pagination: PaginationConfig? = null
)

data class PaginationConfig(
    val type: String, // "offset", "cursor", "page"
    val pageSize: Int = 20,
    val offsetParam: String? = null,
    val limitParam: String? = null,
    val cursorParam: String? = null,
    val pageParam: String? = null
)

data class ItemTemplate(
    val type: String,
    val layout: ComponentNode,
    val actions: Map<String, ActionConfig>? = null
)

data class LoadingState(
    val type: String,
    val count: Int? = null
)

data class LazyColumnItem(
    val id: String,
    val type: String,
    val template: ComponentNode,
    val data: Map<String, Any> = emptyMap()
)

data class ComponentNode(
    val type: String,
    val id: String? = null,
    val properties: Map<String, Any>? = null,
    val modifier: ModifierConfig? = null,
    val children: List<ComponentNode>? = null,
    val actions: Map<String, ActionConfig>? = null,
    val arrangement: String? = null,
    val columns: Int? = null, // For LazyGrid
    val dataSource: DataSource? = null,
    val itemTemplate: ItemTemplate? = null,
    val loadingTemplate: LoadingState? = null,
    val emptyTemplate: ComponentNode? = null,
    val errorTemplate: ComponentNode? = null
)

data class PrecompiledComponent(
    val type: ComponentType,
    val properties: Map<String, Any>,
    val modifier: CompiledModifier,
    val children: List<PrecompiledComponent>,
    val dataSource: DataSource? = null,
    val itemTemplate: ItemTemplate? = null,
    val actions: Map<String, ActionConfig>? = null,
    val factoryComponent: (@Composable () -> Unit)
)

data class CompiledModifier(
    val padding: Int?,
    val fillMaxSize: Boolean,
    val fillMaxWidth: Boolean,
    val width: Int?,
    val height: Int?,
    val weight: Float?
) {
    fun toModifier(): Modifier {
        var modifier = Modifier
        padding?.let { modifier = modifier.padding(it.dp) as Modifier.Companion }
        if (fillMaxSize) modifier = modifier.fillMaxSize() as Modifier.Companion
        if (fillMaxWidth) modifier = modifier.fillMaxWidth() as Modifier.Companion
        width?.let { modifier = modifier.width(it.dp) as Modifier.Companion }
        height?.let { modifier = modifier.height(it.dp) as Modifier.Companion }
        return modifier
    }
}

enum class ComponentType {
    COLUMN, SCROLLABLE_COLUMN, ROW, TEXT, BUTTON, IMAGE, LAZY_COLUMN, ENHANCED_LAZY_COLUMN, LAZY_ROW, LAZY_GRID, CARD, SPACER,
    TOP_APP_BAR, PROGRESS_INDICATOR,
    BAR_CHART, LINE_CHART, PIE_CHART, BUBBLE_CHART, RADAR_CHART,
    UNKNOWN;

    companion object {
        fun fromString(type: String): ComponentType = when (type) {
            "column" -> COLUMN
            "scrollable_column" -> SCROLLABLE_COLUMN
            "enhanced_lazy_column" -> ENHANCED_LAZY_COLUMN
            "row" -> ROW
            "text" -> TEXT
            "button" -> BUTTON
            "image" -> IMAGE
            "lazy_column" -> LAZY_COLUMN
            "lazy_row" -> LAZY_ROW
            "lazy_grid" -> LAZY_GRID
            "card" -> CARD
            "spacer" -> SPACER
            "top_app_bar" -> TOP_APP_BAR
            "progress_indicator" -> PROGRESS_INDICATOR
            "bar_chart" -> BAR_CHART
            "line_chart" -> LINE_CHART
            "pie_chart" -> PIE_CHART
            "bubble_chart" -> BUBBLE_CHART
            "radar_chart" -> RADAR_CHART
            else -> UNKNOWN
        }
    }
}

data class ComponentScreen(
    val version: String,
    val screen: ScreenLayout,
    val theme: ThemeConfig? = null,
    val data: DataConfig? = null
)