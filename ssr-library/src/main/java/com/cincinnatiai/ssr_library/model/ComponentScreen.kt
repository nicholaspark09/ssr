package com.cincinnatiai.ssr_library.model

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class ScreenLayout(
    val id: String,
    val title: String,
    val layout: ComponentNode
)

data class ModifierConfig(
    val padding: Int? = null,
    val fillMaxSize: Boolean? = null,
    val fillMaxWidth: Boolean? = null,
    val width: Int? = null,
    val height: Int? = null,
    val weight: Float? = null
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
    COLUMN, ROW, TEXT, BUTTON, IMAGE, LAZY_COLUMN, LAZY_ROW, LAZY_GRID, CARD, SPACER, UNKNOWN;

    companion object {
        fun fromString(type: String): ComponentType = when (type) {
            "column" -> COLUMN
            "row" -> ROW
            "text" -> TEXT
            "button" -> BUTTON
            "image" -> IMAGE
            "lazy_column" -> LAZY_COLUMN
            "lazy_row" -> LAZY_ROW
            "lazy_grid" -> LAZY_GRID
            "card" -> CARD
            "spacer" -> SPACER
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