package com.cincinnatiai.ssr_library.componentfactory

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cincinnatiai.ssr_library.ComponentFactory
import com.cincinnatiai.ssr_library.model.ActionConfig
import com.cincinnatiai.ssr_library.model.ActionHandler
import com.cincinnatiai.ssr_library.model.CoilImageLoader
import com.cincinnatiai.ssr_library.model.ComponentNode
import com.cincinnatiai.ssr_library.model.DataSource
import com.cincinnatiai.ssr_library.model.DefaultActionHandler
import com.cincinnatiai.ssr_library.model.ImageLoader
import com.cincinnatiai.ssr_library.model.ItemTemplate
import com.cincinnatiai.ssr_library.model.ModifierConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

enum class ComponentComplexity {
    LOW, MEDIUM, HIGH
}

data class ComponentRenderMetadata(
    val requiresAsyncData: Boolean = false,
    val estimatedComplexity: ComponentComplexity = ComponentComplexity.LOW,
    val dependencies: List<String> = emptyList(),
    val cacheKey: String? = null
)

data class CompiledItemTemplate(
    val originalTemplate: ItemTemplate,
    val preparedComponent: ComponentPreparationResult
)

sealed class ValidationResult {
    object Valid : ValidationResult()
    data class Invalid(val errors: List<String>) : ValidationResult()
    data class Warning(val warnings: List<String>) : ValidationResult()
}

sealed class ComponentPreparationResult {
    data class Success(
        val component: @Composable () -> Unit,
        val metadata: ComponentRenderMetadata
    ) : ComponentPreparationResult()

    data class Error(
        val message: String,
        val fallbackComponent: (@Composable () -> Unit)? = null
    ) : ComponentPreparationResult()
}

class DefaultComponentFactory(
    private val actionHandler: ActionHandler = DefaultActionHandler(),
    private val imageLoader: ImageLoader = CoilImageLoader()
) : ComponentFactory {

    private val preparationCache = ConcurrentHashMap<String, ComponentPreparationResult>()

    override suspend fun prepareComponent(node: ComponentNode): ComponentPreparationResult {
        Log.d("SSR_PREPARE", "Preparing component: type='${node.type}', id='${node.id}'")

        if (node.type.isNullOrBlank()) {
            Log.e("SSR_PREPARE", "NULL TYPE ERROR - Node: $node")
            return ComponentPreparationResult.Error("Component type is null")
        }
        val cacheKey = generateCacheKey(node)
        preparationCache[cacheKey]?.let { return it }
        return withContext(Dispatchers.Default) {
            try {
                when (val validation = validateComponent(node)) {
                    is ValidationResult.Invalid -> {
                        return@withContext ComponentPreparationResult.Error(
                            message = "Validation failed: ${validation.errors.joinToString(", ")}",
                            fallbackComponent = {
                                Text("Invalid component: ${node.type}") }
                        )
                    }

                    is ValidationResult.Warning ->
                        Log.w("SSRLibrary", "Component warnings: ${validation.warnings.joinToString(", ")}")

                    ValidationResult.Valid -> {
                        // Continue processing
                    }
                }

                val metadata = analyzeComponentComplexity(node)
                val component = prepareComponentByType(node, metadata)

                val result = ComponentPreparationResult.Success(component, metadata)
                preparationCache[cacheKey] = result
                result

            } catch (e: Exception) {
                ComponentPreparationResult.Error(
                    message = "Failed to prepare component: ${e.message}",
                    fallbackComponent = { Text("Error: ${node.type}") }
                )
            }
        }
    }

    override fun createComponent(node: ComponentNode): @Composable () -> Unit =
        when (node.type) {
            "column" -> { { CreateColumn(node) } }
            "row" -> { { CreateRow(node) } }
            "text" -> { { CreateText(node) } }
            "button" -> { { CreateButton(node) } }
            "image" -> { { CreateImage(node) } }
            "spacer" -> { { CreateSpacer(node) } }
            "lazy_column" -> { { CreateLazyColumn(node) } }
            "lazy_row" -> { { CreateLazyRow(node) } }
            "card" -> { { CreateCard(node) } }
            "top_app_bar" -> { { CreateTopAppBar(node) } }
            "progress_indicator" -> {{ ShowLoadingState(node) }}
            else -> { {
                Text(
                    "Unknown component type: ${node.type}",
                    color = MaterialTheme.colorScheme.error
                )
            } }
        }

    fun validateComponent(node: ComponentNode): ValidationResult {
        val errors = mutableListOf<String>()
        val warnings = mutableListOf<String>()

        // Basic validation
        if (node.type.isBlank()) {
            errors.add("Component type cannot be blank")
        }

        // Type-specific validation
        when (node.type) {
            "text" -> {
                if (node.properties?.get("text") == null) {
                    warnings.add("Text component missing 'text' property")
                }
            }

            "button" -> {
                if (node.properties?.get("text") == null) {
                    errors.add("Button component requires 'text' property")
                }
            }

            "image" -> {
                if (node.properties?.get("url") == null) {
                    errors.add("Image component requires 'url' property")
                }
            }

            "lazy_column", "lazy_row" -> {
                if (node.dataSource == null) {
                    errors.add("Lazy components require dataSource")
                }
                if (node.itemTemplate == null) {
                    errors.add("Lazy components require itemTemplate")
                }
            }
            "top_app_bar" -> {
                if (node.properties?.get("title") == null) {
                    warnings.add("TopAppBar missing 'title' property")
                }
            }
        }

        // Validate children recursively
        node.children?.forEach { child ->
            when (val childValidation = validateComponent(child)) {
                is ValidationResult.Invalid -> errors.addAll(childValidation.errors)
                is ValidationResult.Warning -> warnings.addAll(childValidation.warnings)
                ValidationResult.Valid -> { /* Continue */
                }
            }
        }

        return when {
            errors.isNotEmpty() -> ValidationResult.Invalid(errors)
            warnings.isNotEmpty() -> ValidationResult.Warning(warnings)
            else -> ValidationResult.Valid
        }
    }

    private suspend fun prepareComponentByType(
        node: ComponentNode,
        metadata: ComponentRenderMetadata
    ): @Composable () -> Unit {
        return when (metadata.estimatedComplexity) {
            ComponentComplexity.HIGH -> {
                // For high complexity components, do more preparation
                prepareComplexComponent(node)
            }
            ComponentComplexity.MEDIUM -> {
                // Medium complexity - some optimization
                prepareMediumComponent(node)
            }
            ComponentComplexity.LOW -> {
                // Low complexity - standard preparation
                createComponent(node)
            }
        }
    }

    private suspend fun prepareComplexComponent(node: ComponentNode): @Composable () -> Unit {
        return when (node.type) {
            "lazy_column", "lazy_row" -> {
                prepareLazyComponent(node)
            }
            else -> createComponent(node)
        }
    }

    private suspend fun prepareMediumComponent(node: ComponentNode): @Composable () -> Unit {
        return createComponent(node)
    }

    private suspend fun prepareLazyComponent(node: ComponentNode): @Composable () -> Unit {
        // Pre-validate data source and template
        val dataSource = node.dataSource
        val itemTemplate = node.itemTemplate

        if (dataSource == null || itemTemplate == null) {
            return { Text("Invalid lazy component configuration") }
        }

        // Pre-compile item template
        val compiledTemplate = withContext(Dispatchers.Default) {
            compileItemTemplate(itemTemplate)
        }

        return {
            when (node.type) {
                "lazy_column" -> AsyncLazyColumn(node, compiledTemplate)
                "lazy_row" -> AsyncLazyRow(node, compiledTemplate)
                else -> Text("Unknown lazy component: ${node.type}")
            }
        }
    }

    private suspend fun compileItemTemplate(itemTemplate: ItemTemplate): CompiledItemTemplate {
        return withContext(Dispatchers.Default) {
            CompiledItemTemplate(
                originalTemplate = itemTemplate,
                preparedComponent = prepareComponent(itemTemplate.layout)
            )
        }
    }

    private fun analyzeComponentComplexity(node: ComponentNode): ComponentRenderMetadata {
        val requiresAsyncData = when (node.type) {
            "lazy_column", "lazy_row" -> node.dataSource?.type == "api"
            "image" -> node.properties?.get("url")?.toString()?.startsWith("http") == true
            else -> false
        }

        val complexity = when {
            node.type in listOf("lazy_column", "lazy_row") -> ComponentComplexity.HIGH
            (node.children?.size ?: 0) > 10 -> ComponentComplexity.MEDIUM
            node.type == "image" && requiresAsyncData -> ComponentComplexity.MEDIUM
            else -> ComponentComplexity.LOW
        }

        val dependencies = mutableListOf<String>().apply {
            if (requiresAsyncData) add("network")
            if (node.type == "image") add("image_loader")
            if (node.actions?.isNotEmpty() == true) add("action_handler")
        }

        return ComponentRenderMetadata(
            requiresAsyncData = requiresAsyncData,
            estimatedComplexity = complexity,
            dependencies = dependencies,
            cacheKey = generateCacheKey(node)
        )
    }

    @Composable
    private fun AsyncLazyColumn(
        node: ComponentNode,
        compiledTemplate: CompiledItemTemplate
    ) {
        val dataSource = node.dataSource ?: return

        when (dataSource.type) {
            "static" -> StaticLazyColumn(node, dataSource, compiledTemplate)
            "api" -> ApiLazyColumn(node, dataSource, compiledTemplate)
            else -> Text("Unsupported data source: ${dataSource.type}")
        }
    }

    @Composable
    private fun AsyncLazyRow(
        node: ComponentNode,
        compiledTemplate: CompiledItemTemplate
    ) {
        val dataSource = node.dataSource ?: return

        when (dataSource.type) {
            "static" -> StaticLazyRow(node, dataSource, compiledTemplate)
            "api" -> ApiLazyRow(node, dataSource, compiledTemplate)
            else -> Text("Unsupported data source: ${dataSource.type}")
        }
    }

    @Composable
    private fun ApiLazyRow(
        node: ComponentNode,
        dataSource: DataSource,
        compiledTemplate: CompiledItemTemplate
    ) {
        var items by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
        var isLoading by remember { mutableStateOf(true) }

        LaunchedEffect(dataSource.url) {
            loadApiData(dataSource, 0) { result ->
                when (result) {
                    is ApiResult.Success -> {
                        items = result.data
                        isLoading = false
                    }
                    is ApiResult.Error -> {
                        isLoading = false
                    }
                }
            }
        }

        if (isLoading) {
            Box(
                modifier = createModifier(node.modifier),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyRow(
                modifier = createModifier(node.modifier),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { item ->
                    RenderCompiledItem(compiledTemplate, item)
                }
            }
        }
    }

    private suspend fun loadApiData(
        dataSource: DataSource,
        page: Int,
        callback: (ApiResult) -> Unit
    ) {
        try {
            withContext(Dispatchers.IO) {
                // Simulate network delay
                delay(1000)

                // Mock API response
                val mockData = generateMockData(page, dataSource.pagination?.pageSize ?: 20)
                callback(ApiResult.Success(mockData))
            }
        } catch (e: Exception) {
            callback(ApiResult.Error(e.message ?: "Network error"))
        }
    }

    private fun generateMockData(page: Int, pageSize: Int): List<Map<String, Any>> {
        val startIndex = page * pageSize
        return (startIndex until startIndex + pageSize).map { index ->
            mapOf(
                "id" to "$index",
                "title" to "Item $index",
                "description" to "This is item number $index with some description text",
                "imageUrl" to "https://picsum.photos/200/200?random=$index",
                "price" to (10..100).random(),
                "category" to listOf("Electronics", "Clothing", "Books", "Home").random()
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun CreateTopAppBar(node: ComponentNode) {
        val props = node.properties ?: emptyMap()
        val title = props["title"] as? String ?: ""
        val centerTitle = props["centerTitle"] as? Boolean ?: false
        val backgroundColor = props["backgroundColor"] as? String
        val titleColor = props["titleColor"] as? String

        Log.d("SSR_APP_BAR", "Creating TopAppBar: title='$title', center=$centerTitle")

        // Parse colors
        val bgColor = backgroundColor?.let { parseColor(it) } ?: MaterialTheme.colorScheme.surface
        val textColor = titleColor?.let { parseColor(it) } ?: MaterialTheme.colorScheme.onSurface

        if (centerTitle) {
            // Centered title version
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = title,
                        color = textColor,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = bgColor,
                    titleContentColor = textColor
                ),
                modifier = createModifier(node.modifier)
            )
        } else {
            // Regular left-aligned version
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        color = textColor,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = bgColor,
                    titleContentColor = textColor
                ),
                modifier = createModifier(node.modifier)
            )
        }
    }

    private fun parseColor(colorString: String): Color {
        return try {
            val cleanColor = colorString.removePrefix("#")
            val colorLong = cleanColor.toLong(16)

            when (cleanColor.length) {
                6 -> Color(0xFF000000 or colorLong) // Add full alpha
                8 -> Color(colorLong) // Already includes alpha
                3 -> {
                    // Handle short hex format like #RGB -> #RRGGBB
                    val r = cleanColor[0]
                    val g = cleanColor[1]
                    val b = cleanColor[2]
                    val expandedColor = "$r$r$g$g$b$b"
                    Color(0xFF000000 or expandedColor.toLong(16))
                }
                else -> Color.Black
            }
        } catch (e: NumberFormatException) {
            // Named colors support
            when (colorString.lowercase().trim()) {
                "white" -> Color.White
                "black" -> Color.Black
                "red" -> Color.Red
                "green" -> Color.Green
                "blue" -> Color.Blue
                "gray", "grey" -> Color.Gray
                "yellow" -> Color.Yellow
                "cyan" -> Color.Cyan
                "magenta" -> Color.Magenta
                "transparent" -> Color.Transparent
                else -> Color.Black // Default fallback
            }
        }
    }

    @Composable
    private fun StaticLazyColumn(
        node: ComponentNode,
        dataSource: DataSource,
        compiledTemplate: CompiledItemTemplate
    ) {
        val items = dataSource.items ?: emptyList()
        Log.d("SSR_STATIC", "StaticLazyColumn rendering ${items.size} items")

        if (items.isEmpty()) {
            node.emptyTemplate?.let { emptyTemplate ->
                val emptyComponent = createComponent(emptyTemplate)
                emptyComponent()
            } ?: Box(
                modifier = createModifier(node.modifier),
                contentAlignment = Alignment.Center
            ) {
                Text("No items to display")
            }
        } else {
            LazyColumn(
                modifier = createModifier(node.modifier),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { item ->
                    Log.d("SSR_STATIC", "Rendering item: $item")

                    val wrappedData = mapOf("item" to item)

                    val boundTemplate = bindDataToTemplate(compiledTemplate.originalTemplate.layout, wrappedData)
                    val itemComponent = createComponent(boundTemplate)

                    val clickHandler = compiledTemplate.originalTemplate.actions?.get("onClick")?.let { action ->
                        val boundAction = bindDataToAction(action, wrappedData)
                        return@let {
                            Log.d("SSR_STATIC_ACTION", "Item clicked: ${boundAction.destination}")
                            actionHandler.handleAction(boundAction)
                        }
                    }

                    if (clickHandler != null) {
                        Box(
                            modifier = Modifier.clickable { clickHandler() }
                        ) {
                            itemComponent()
                        }
                    } else {
                        itemComponent()
                    }
                }
            }
        }
    }

    @Composable
    private fun StaticLazyRow(
        node: ComponentNode,
        dataSource: DataSource,
        compiledTemplate: CompiledItemTemplate
    ) {
        val items = dataSource.items ?: emptyList()

        LazyRow(
            modifier = createModifier(node.modifier),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) { item ->
                RenderCompiledItem(compiledTemplate, item)
            }
        }
    }

    @Composable
    private fun RenderCompiledItem(
        compiledTemplate: CompiledItemTemplate,
        item: Map<String, Any>
    ) {
        when (val result = compiledTemplate.preparedComponent) {
            is ComponentPreparationResult.Success -> {
                // Bind data to the template
                val boundTemplate = bindDataToTemplate(compiledTemplate.originalTemplate.layout, item)
                val itemComponent = createComponent(boundTemplate)

                // Handle click actions
                val clickHandler = compiledTemplate.originalTemplate.actions?.get("onClick")?.let { action ->
                    val boundAction = bindDataToAction(action, item)
                    return@let { actionHandler.handleAction(boundAction) }
                }

                if (clickHandler != null) {
                    Box(
                        modifier = Modifier.clickable { clickHandler() }
                    ) {
                        itemComponent()
                    }
                } else {
                    itemComponent()
                }
            }
            is ComponentPreparationResult.Error -> {
                result.fallbackComponent?.invoke() ?: Text("Error rendering item")
            }
        }
    }

    private fun bindDataToTemplate(template: ComponentNode, data: Map<String, Any>): ComponentNode {
        Log.d("SSR_BINDING", "Binding template ${template.type} with data keys: ${data.keys}, values: ${data.values}")

        val boundProperties = template.properties?.mapValues { (key, value) ->
            if (value is String && value.contains("{{")) {
                val boundValue = bindStringTemplate(value, data)
                Log.d("SSR_BINDING", "Property $key: '$value' -> '$boundValue'")
                boundValue
            } else {
                value
            }
        }

        val boundChildren = template.children?.map { child ->
            bindDataToTemplate(child, data)
        }

        return template.copy(
            properties = boundProperties,
            children = boundChildren
        )
    }

    private fun bindDataToAction(action: ActionConfig, data: Map<String, Any>): ActionConfig {
        val boundParams = action.params?.mapValues { (_, value) ->
            bindStringTemplate(value, data)
        }

        return action.copy(params = boundParams)
    }

    private fun bindStringTemplate(template: String, data: Map<String, Any>): String {
        var result = template
        val regex = "\\{\\{([^}]+)\\}\\}".toRegex()

        Log.d("SSR_BINDING", "Binding template: '$template'")
        Log.d("SSR_BINDING", "Available data keys: ${data.keys}")
        Log.d("SSR_BINDING", "Full data structure: $data")

        regex.findAll(template).forEach { match ->
            val key = match.groupValues[1].trim()
            Log.d("SSR_BINDING", "Looking for key: '$key'")

            val value = getNestedValue(data, key)
            Log.d("SSR_BINDING", "Found value for '$key': $value (type: ${value?.javaClass?.simpleName})")

            val stringValue = when (value) {
                is Number -> value.toString()
                is String -> value
                is Boolean -> value.toString()
                null -> {
                    Log.w("SSR_BINDING", "NULL value for key '$key' in data: $data")
                    ""
                }
                else -> value.toString()
            }

            result = result.replace(match.value, stringValue)
            Log.d("SSR_BINDING", "Replaced '${match.value}' with '$stringValue'")
        }

        Log.d("SSR_BINDING", "Final result: '$result'")
        return result
    }

    private fun getNestedValue(data: Map<String, Any>, key: String): Any? {
        Log.d("SSR_NESTED", "Getting nested value for key: '$key'")

        val parts = key.split(".")
        var current: Any? = data

        for (i in parts.indices) {
            val part = parts[i]
            Log.d("SSR_NESTED", "Looking for part '$part' in: ${current?.javaClass?.simpleName}")

            current = when (current) {
                is Map<*, *> -> {
                    val value = current[part]
                    Log.d("SSR_NESTED", "Found '$part' = $value")
                    value
                }
                else -> {
                    Log.w("SSR_NESTED", "Cannot access '$part' - current is not a Map: $current")
                    null
                }
            }

            if (current == null) {
                Log.w("SSR_NESTED", "Null value at part '$part' - stopping traversal")
                break
            }
        }

        Log.d("SSR_NESTED", "Final nested value: $current")
        return current
    }

    sealed class ApiResult {
        data class Success(val data: List<Map<String, Any>>) : ApiResult()
        data class Error(val message: String) : ApiResult()
    }

    @Composable
    private fun ApiLazyColumn(
        node: ComponentNode,
        dataSource: DataSource,
        compiledTemplate: CompiledItemTemplate
    ) {
        var items by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
        var isLoading by remember { mutableStateOf(true) }
        var hasError by remember { mutableStateOf(false) }
        var canLoadMore by remember { mutableStateOf(true) }
        var currentPage by remember { mutableStateOf(0) }

        LaunchedEffect(dataSource.url) {
            loadApiData(dataSource, 0) { result ->
                when (result) {
                    is ApiResult.Success -> {
                        items = result.data
                        isLoading = false
                        hasError = false
                    }
                    is ApiResult.Error -> {
                        isLoading = false
                        hasError = true
                    }
                }
            }
        }

        when {
            isLoading && items.isEmpty() -> {
                ShowLoadingState(node)
            }
            hasError && items.isEmpty() -> {
                ShowErrorState(node) {
                    isLoading = true
                    hasError = false
                }
            }
            items.isEmpty() -> {
                ShowEmptyState(node)
            }
            else -> {
                LazyColumn(
                    modifier = createModifier(node.modifier)
                ) {
                    itemsIndexed(items) { index, item ->
                        RenderCompiledItem(compiledTemplate, item)

                        // Load more when reaching near end
                        if (index >= items.size - 3 && canLoadMore && !isLoading) {
                            LaunchedEffect(index) {
                                loadApiData(dataSource, currentPage + 1) { result ->
                                    when (result) {
                                        is ApiResult.Success -> {
                                            if (result.data.isNotEmpty()) {
                                                items = items + result.data
                                                currentPage++
                                            } else {
                                                canLoadMore = false
                                            }
                                        }
                                        is ApiResult.Error -> {
                                            // Handle pagination error
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (isLoading && items.isNotEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ShowEmptyState(node: ComponentNode) {
        node.emptyTemplate?.let { emptyTemplate ->
            val emptyComponent = createComponent(emptyTemplate)
            emptyComponent()
        } ?: Box(
            modifier = createModifier(node.modifier),
            contentAlignment = Alignment.Center
        ) {
            Text("No items found")
        }
    }

    @Composable
    private fun ShowLoadingState(node: ComponentNode) {
        node.loadingTemplate?.let { loadingTemplate ->
            when (loadingTemplate.type) {
                "shimmer" -> ShimmerLoading(loadingTemplate.count ?: 5)
                else -> CircularProgressIndicator()
            }
        } ?: Box(
            modifier = createModifier(node.modifier),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    @Composable
    private fun ShowErrorState(node: ComponentNode, onRetry: () -> Unit) {
        node.errorTemplate?.let { errorTemplate ->
            val errorComponent = createComponent(errorTemplate)
            errorComponent()
        } ?: Column(
            modifier = createModifier(node.modifier),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Failed to load data")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }

    @Composable
    private fun ShimmerLoading(count: Int) {
        Column {
            repeat(count) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    Color.Gray.copy(alpha = 0.3f),
                                    RoundedCornerShape(4.dp)
                                )
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .height(16.dp)
                                    .background(
                                        Color.Gray.copy(alpha = 0.3f),
                                        RoundedCornerShape(4.dp)
                                    )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .height(12.dp)
                                    .background(
                                        Color.Gray.copy(alpha = 0.2f),
                                        RoundedCornerShape(4.dp)
                                    )
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun CreateColumn(node: ComponentNode) {
        val props = node.properties ?: emptyMap()
        val backgroundColor = props["backgroundColor"] as? String

        // Parse background color
        val bgColor = backgroundColor?.let { parseColor(it) }

        var modifier = createModifier(node.modifier)

        // Apply background color if specified
        if (bgColor != null) {
            modifier = modifier.background(bgColor)
        }

        Column(
            modifier = modifier
        ) {
            node.children?.forEach { child ->
                val childWeight = child.modifier?.weight
                if (childWeight != null) {
                    // Child has weight - apply it in ColumnScope
                    Box(
                        modifier = Modifier.weight(childWeight)
                    ) {
                        // Create child component with its own modifier (excluding weight)
                        val childModifierWithoutWeight = child.modifier.copy(weight = null)
                        val childWithoutWeight = child.copy(modifier = childModifierWithoutWeight)
                        val childComponent = createComponent(childWithoutWeight)
                        childComponent()
                    }
                } else {
                    // No weight - render normally
                    val childComponent = createComponent(child)
                    childComponent()
                }
            }
        }
    }
    @Composable
    private fun CreateRow(node: ComponentNode) {
        Row(
            modifier = createModifier(node.modifier)
        ) {
            node.children?.forEach { child ->
                val childWeight = child.modifier?.weight

                if (childWeight != null) {
                    // Child has weight - apply it in RowScope
                    Box(
                        modifier = Modifier.weight(childWeight)
                    ) {
                        // Create child component with its own modifier (excluding weight)
                        val childModifierWithoutWeight = child.modifier?.copy(weight = null)
                        val childWithoutWeight = child.copy(modifier = childModifierWithoutWeight)
                        val childComponent = createComponent(childWithoutWeight)
                        childComponent()
                    }
                } else {
                    // No weight - render normally
                    val childComponent = createComponent(child)
                    childComponent()
                }
            }
        }
    }

    @Composable
    private fun CreateText(node: ComponentNode) {
        val props = node.properties ?: emptyMap()
        val text = props["text"] as? String ?: ""
        val style = when (props["style"] as? String) {
            "headline1" -> MaterialTheme.typography.headlineLarge
            "headline2" -> MaterialTheme.typography.headlineMedium
            "subtitle1" -> MaterialTheme.typography.titleMedium
            "body1" -> MaterialTheme.typography.bodyLarge
            "body2" -> MaterialTheme.typography.bodyMedium
            else -> MaterialTheme.typography.bodyLarge
        }

        Log.d("SSR_TEXT", "Creating text component with text: '$text', style: ${props["style"]}")

        Text(
            text = text,
            style = style,
            modifier = createModifier(node.modifier)
        )
    }

    @Composable
    private fun CreateButton(node: ComponentNode) {
        val props = node.properties ?: emptyMap()
        val text = props["text"] as? String ?: ""
        val enabled = props["enabled"] as? Boolean ?: true

        val onClick = node.actions?.get("onClick")?.let { action ->
            { actionHandler.handleAction(action) }
        } ?: { }

        Button(
            onClick = onClick,
            enabled = enabled,
            modifier = createModifier(node.modifier)
        ) {
            Text(text)
        }
    }

    @Composable
    private fun CreateImage(node: ComponentNode) {
        val props = node.properties ?: emptyMap()
        val url = props["url"] as? String ?: ""
        val shape = props["shape"] as? String
        val size = (props["size"] as? Number)?.toInt()
        val alignment = (props["alignment"] as? String) ?: ""

        var modifier = createModifier(node.modifier)

        // Apply size if specified - for circles, make it square
        if (size != null) {
            modifier = modifier.size(size.dp)
        }

        // Center the image
        if (alignment == "center") {
            modifier = modifier.wrapContentSize(Alignment.Center)
        }

        // Use custom image loader if available
        imageLoader.LoadImage(
            url = url,
            modifier = if (shape == "circle") {
                modifier
                    .aspectRatio(1f)
                    .clip(CircleShape)
            } else {
                modifier
            }
        )
    }

    @Composable
    private fun CreateSpacer(node: ComponentNode) {
        val props = node.properties ?: emptyMap()
        val height = props["height"] as? Int
        val width = props["width"] as? Int

        when {
            height != null && width != null -> {
                Spacer(modifier = Modifier.size(width.dp, height.dp))
            }
            height != null -> {
                Spacer(modifier = Modifier.height(height.dp))
            }
            width != null -> {
                Spacer(modifier = Modifier.width(width.dp))
            }
            else -> {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

    @Composable
    private fun CreateLazyColumn(node: ComponentNode) {
        val dataSource = node.dataSource
        val itemTemplate = node.itemTemplate

        if (dataSource != null && itemTemplate != null) {
            when (dataSource.type) {
                "static" -> {
                    val items = dataSource.items ?: emptyList()
                    Log.d("SSR_LAZY", "Creating static LazyColumn with ${items.size} items")

                    if (items.isEmpty()) {
                        Box(
                            modifier = createModifier(node.modifier),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No items to display")
                        }
                    } else {
                        LazyColumn(
                            modifier = createModifier(node.modifier),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(items) { item ->
                                Log.d("SSR_ITEM", "Original item: $item")

                                val wrappedData = mapOf("item" to item)
                                Log.d("SSR_ITEM", "Wrapped data: $wrappedData")

                                // Bind data to the template
                                val boundTemplate = bindDataToTemplate(itemTemplate.layout, wrappedData)
                                val itemComponent = createComponent(boundTemplate)

                                // Handle click actions
                                val clickHandler = itemTemplate.actions?.get("onClick")?.let { action ->
                                    val boundAction = bindDataToAction(action, wrappedData)
                                    return@let {
                                        Log.d("SSR_ACTION", "Item clicked: ${boundAction.destination}")
                                        actionHandler.handleAction(boundAction)
                                    }
                                }

                                if (clickHandler != null) {
                                    Box(
                                        modifier = Modifier.clickable { clickHandler() }
                                    ) {
                                        itemComponent()
                                    }
                                } else {
                                    itemComponent()
                                }
                            }
                        }
                    }
                }
                "api" -> {
                    Text("API LazyColumn not implemented in simple version")
                }
                else -> {
                    Text("Unsupported data source type: ${dataSource.type}")
                }
            }
        } else {
            Text("LazyColumn requires dataSource and itemTemplate")
        }
    }

    @Composable
    private fun CreateLazyRow(node: ComponentNode) {
        val dataSource = node.dataSource
        val itemTemplate = node.itemTemplate

        if (dataSource != null && itemTemplate != null) {
            when (dataSource.type) {
                "static" -> {
                    val items = dataSource.items ?: emptyList()
                    LazyRow(
                        modifier = createModifier(node.modifier)
                    ) {
                        items(items) { item ->
                            val boundTemplate = bindDataToTemplate(itemTemplate.layout, item)
                            val itemComponent = createComponent(boundTemplate)
                            itemComponent()
                        }
                    }
                }
                else -> {
                    Text("Unsupported data source type for LazyRow: ${dataSource.type}")
                }
            }
        }
    }

    @Composable
    private fun CreateCard(node: ComponentNode) {
        val props = node.properties ?: emptyMap()
        val elevation = (props["elevation"] as? Number)?.toFloat() ?: 1f
        val backgroundColor = props["backgroundColor"] as? String
        val bgColor = backgroundColor?.let { parseColor(it) } ?: MaterialTheme.colorScheme.surface

        Card(
            modifier = createModifier(node.modifier),
            elevation = CardDefaults.cardElevation(defaultElevation = elevation.dp),
            colors = CardDefaults.cardColors(containerColor = bgColor)
        ) {
            node.children?.forEach { child ->
                val childComponent = createComponent(child)
                childComponent()
            }
        }
    }

    private fun createModifier(modifierConfig: ModifierConfig?): Modifier {
        var modifier: Modifier = Modifier
        modifierConfig?.let { config ->
            config.padding?.let { padding ->
                modifier = modifier.padding(padding.dp)
            }
            if (config.fillMaxSize == true) {
                modifier = modifier.fillMaxSize()
            }
            if (config.fillMaxWidth == true) {
                modifier = modifier.fillMaxWidth()
            }
            config.width?.let { width ->
                modifier = modifier.width(width.dp)
            }
            config.height?.let { height ->
                modifier = modifier.height(height.dp)
            }
        }
        return modifier
    }

    private fun generateCacheKey(node: ComponentNode): String {
        return "${node.type}_${node.hashCode()}"
    }
}