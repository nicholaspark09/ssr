package com.cincinnatiai.ssr_library.componentfactory

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.cincinnatiai.ssr_library.component.charts.BarChart
import com.cincinnatiai.ssr_library.component.charts.BubbleChart
import com.cincinnatiai.ssr_library.component.charts.ChartConfig
import com.cincinnatiai.ssr_library.component.charts.ChartDataPoint
import com.cincinnatiai.ssr_library.component.charts.ChartSeries
import com.cincinnatiai.ssr_library.component.charts.LineChart
import com.cincinnatiai.ssr_library.component.charts.PieChart
import com.cincinnatiai.ssr_library.component.charts.RadarChart
import com.cincinnatiai.ssr_library.component.charts.parseColor
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
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap

enum class ComponentComplexity {
    LOW, MEDIUM, HIGH
}

data class ComponentRenderMetadata(
    val requiresAsyncData: Boolean = false,
    val estimatedComplexity: ComponentComplexity = ComponentComplexity.LOW,
    val dependencies: List<String> = emptyList(),
    val cacheKey: String? = null,
)

data class CompiledItemTemplate(
    val originalTemplate: ItemTemplate,
    val preparedComponent: ComponentPreparationResult,
)

sealed class ValidationResult {
    object Valid : ValidationResult()
    data class Invalid(val errors: List<String>) : ValidationResult()
    data class Warning(val warnings: List<String>) : ValidationResult()
}

sealed class ComponentPreparationResult {
    data class Success(
        val component: @Composable () -> Unit,
        val metadata: ComponentRenderMetadata,
    ) : ComponentPreparationResult()

    data class Error(
        val message: String,
        val fallbackComponent: (@Composable () -> Unit)? = null,
    ) : ComponentPreparationResult()
}

class DefaultComponentFactory(
    private val actionHandler: ActionHandler = DefaultActionHandler(),
    private val imageLoader: ImageLoader = CoilImageLoader(),
) : ComponentFactory {

    private val preparationCache = ConcurrentHashMap<String, ComponentPreparationResult>()

    override suspend fun prepareComponent(node: ComponentNode): ComponentPreparationResult {
        Timber.tag("SSR_PREPARE").d("Preparing component: type='${node.type}', id='${node.id}'")

        if (node.type.isBlank()) {
            Timber.tag("SSR_PREPARE").e("NULL TYPE ERROR - Node: $node")
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
                                Text("Invalid component: ${node.type}")
                            }
                        )
                    }

                    is ValidationResult.Warning ->
                        Timber.tag("SSRLibrary")
                            .w("Component warnings: ${validation.warnings.joinToString(", ")}")

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
            "column" -> {
                { CreateColumn(node) }
            }

            "scrollable_column" -> {
                { CreateScrollableColumn(node) }
            }

            "row" -> {
                { CreateRow(node) }
            }

            "text" -> {
                { CreateText(node) }
            }

            "button" -> {
                { CreateButton(node) }
            }

            "image" -> {
                { CreateImage(node) }
            }

            "spacer" -> {
                { CreateSpacer(node) }
            }

            "lazy_column" -> {
                { CreateLazyColumn(node) }
            }

            "enhanced_lazy_column" -> {
                { CreateEnhancedLazyColumn(node) }
            }

            "lazy_row" -> {
                { CreateLazyRow(node) }
            }

            "card" -> {
                { CreateCard(node) }
            }

            "top_app_bar" -> {
                { CreateTopAppBar(node) }
            }

            "progress_indicator" -> {
                { ShowLoadingState(node) }
            }

            "bar_chart" -> {
                { CreateBarChart(node) }
            }

            "line_chart" -> {
                { CreateLineChart(node) }
            }

            "pie_chart" -> {
                { CreatePieChart(node) }
            }

            "bubble_chart" -> {
                { CreateBubbleChart(node) }
            }

            "radar_chart" -> {
                { CreateRadarChart(node) }
            }

            else -> {
                {
                    Text(
                        "Unknown component type: ${node.type}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

    fun validateComponent(node: ComponentNode): ValidationResult {
        val errors = mutableListOf<String>()
        val warnings = mutableListOf<String>()

        if (node.type.isBlank()) {
            errors.add("Component type cannot be blank")
        }

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

            "bar_chart", "pie_chart", "bubble_chart", "radar_chart" -> {
                if (node.properties?.get("data") == null) {
                    errors.add("${node.type} requires 'data' property")
                }
            }

            "line_chart" -> {
                if (node.properties?.get("series") == null) {
                    errors.add("Line chart requires 'series' property")
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
        metadata: ComponentRenderMetadata,
    ): @Composable () -> Unit {
        return when (metadata.estimatedComplexity) {
            ComponentComplexity.HIGH -> prepareComplexComponent(node)
            ComponentComplexity.MEDIUM -> prepareMediumComponent(node)
            ComponentComplexity.LOW -> createComponent(node)
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
        compiledTemplate: CompiledItemTemplate,
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
        compiledTemplate: CompiledItemTemplate,
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
        compiledTemplate: CompiledItemTemplate,
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

    // This is a mock
    private suspend fun loadApiData(
        dataSource: DataSource,
        page: Int,
        callback: (ApiResult) -> Unit,
    ) {
        try {
            withContext(Dispatchers.IO) {
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

        Timber.tag("SSR_APP_BAR").d("Creating TopAppBar: title='$title', center=$centerTitle")

        val bgColor = backgroundColor?.let { parseColor(it) } ?: MaterialTheme.colorScheme.surface
        val textColor = titleColor?.let { parseColor(it) } ?: MaterialTheme.colorScheme.onSurface

        if (centerTitle) {
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

    @Composable
    private fun StaticLazyColumn(
        node: ComponentNode,
        dataSource: DataSource,
        compiledTemplate: CompiledItemTemplate,
    ) {
        val items = dataSource.items ?: emptyList()
        Timber.tag("SSR_STATIC").d("StaticLazyColumn rendering ${items.size} items")

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
                    Timber.tag("SSR_STATIC").d("Rendering item: $item")

                    val wrappedData = mapOf("item" to item)

                    val boundTemplate =
                        bindDataToTemplate(compiledTemplate.originalTemplate.layout, wrappedData)
                    val itemComponent = createComponent(boundTemplate)

                    val clickHandler =
                        compiledTemplate.originalTemplate.actions?.get("onClick")?.let { action ->
                            val boundAction = bindDataToAction(action, wrappedData)
                            return@let {
                                Timber.tag("SSR_STATIC_ACTION")
                                    .d("Item clicked: ${boundAction.destination}")
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
        compiledTemplate: CompiledItemTemplate,
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
        item: Map<String, Any>,
    ) {
        when (val result = compiledTemplate.preparedComponent) {
            is ComponentPreparationResult.Success -> {
                val boundTemplate =
                    bindDataToTemplate(compiledTemplate.originalTemplate.layout, item)
                val itemComponent = createComponent(boundTemplate)

                val clickHandler =
                    compiledTemplate.originalTemplate.actions?.get("onClick")?.let { action ->
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
        Timber.tag("SSR_BINDING")
            .d("Binding template ${template.type} with data keys: ${data.keys}, values: ${data.values}")

        val boundProperties = template.properties?.mapValues { (key, value) ->
            if (value is String && value.contains("{{")) {
                val boundValue = bindStringTemplate(value, data)
                Timber.tag("SSR_BINDING").d("Property $key: '$value' -> '$boundValue'")
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

        Timber.tag("SSR_BINDING").d("Binding template: '$template'")
        Timber.tag("SSR_BINDING").d("Available data keys: ${data.keys}")
        Timber.tag("SSR_BINDING").d("Full data structure: $data")

        regex.findAll(template).forEach { match ->
            val key = match.groupValues[1].trim()
            Timber.tag("SSR_BINDING").d("Looking for key: '$key'")

            val value = getNestedValue(data, key)
            Timber.tag("SSR_BINDING")
                .d("Found value for '$key': $value (type: ${value?.javaClass?.simpleName})")

            val stringValue = when (value) {
                is Number -> value.toString()
                is String -> value
                is Boolean -> value.toString()
                null -> {
                    Timber.tag("SSR_BINDING").w("NULL value for key '$key' in data: $data")
                    ""
                }

                else -> value.toString()
            }

            result = result.replace(match.value, stringValue)
            Timber.tag("SSR_BINDING").d("Replaced '${match.value}' with '$stringValue'")
        }

        Timber.tag("SSR_BINDING").d("Final result: '$result'")
        return result
    }

    private fun getNestedValue(data: Map<String, Any>, key: String): Any? {
        Timber.tag("SSR_NESTED").d("Getting nested value for key: '$key'")

        val parts = key.split(".")
        var current: Any? = data

        for (i in parts.indices) {
            val part = parts[i]
            Timber.tag("SSR_NESTED")
                .d("Looking for part '$part' in: ${current?.javaClass?.simpleName}")

            current = when (current) {
                is Map<*, *> -> {
                    val value = current[part]
                    Timber.tag("SSR_NESTED").d("Found '$part' = $value")
                    value
                }

                else -> {
                    Timber.tag("SSR_NESTED")
                        .w("Cannot access '$part' - current is not a Map: $current")
                    null
                }
            }

            if (current == null) {
                Timber.tag("SSR_NESTED").w("Null value at part '$part' - stopping traversal")
                break
            }
        }

        Timber.tag("SSR_NESTED").d("Final nested value: $current")
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
        compiledTemplate: CompiledItemTemplate,
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
    private fun CreateScrollableColumn(node: ComponentNode) {
        val props = node.properties ?: emptyMap()
        val backgroundColor = props["backgroundColor"] as? String
        val scrollState = rememberScrollState()

        val bgColor = backgroundColor?.let { parseColor(it) }

        var modifier = createModifier(node.modifier)
            .verticalScroll(scrollState)

        if (bgColor != null) {
            modifier = modifier.background(bgColor)
        }

        Column(
            modifier = modifier
        ) {
            node.children?.forEach { child ->
                val childWeight = child.modifier?.weight
                if (childWeight != null) {
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
                    val childComponent = createComponent(child)
                    childComponent()
                }
            }
        }
    }

    @Composable
    private fun CreateRow(node: ComponentNode) {
        val props = node.properties ?: emptyMap()
        var horizontalArrangement = Arrangement.Start
        val horizontalSetting = props["horizontalArrangement"] as? String ?: ""
        when(horizontalSetting) {
            "end" -> horizontalArrangement = Arrangement.End
            "center" -> horizontalArrangement = Arrangement.Center
            "spaceBetween" -> horizontalArrangement = Arrangement.SpaceBetween
            "spaceEvenly" -> horizontalArrangement = Arrangement.SpaceEvenly
            "spaceAround" -> horizontalArrangement = Arrangement.SpaceAround
        }
        Row(
            modifier = createModifier(node.modifier),
            horizontalArrangement = horizontalArrangement
        ) {
            node.children?.forEach { child ->
                val childWeight = child.modifier?.weight

                if (childWeight != null) {
                    Box(
                        modifier = Modifier.weight(childWeight)
                    ) {
                        val childModifierWithoutWeight = child.modifier?.copy(weight = null)
                        val childWithoutWeight = child.copy(modifier = childModifierWithoutWeight)
                        val childComponent = createComponent(childWithoutWeight)
                        childComponent()
                    }
                } else {
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

        Timber.tag("SSR_TEXT")
            .d("Creating text component with text: '$text', style: ${props["style"]}")

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

        if (size != null) {
            modifier = modifier.size(size.dp)
        }
        if (alignment == "center") {
            modifier = modifier.wrapContentSize(Alignment.Center)
        }

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
                    Timber.tag("SSR_LAZY").d("Creating static LazyColumn with ${items.size} items")

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
                                Timber.tag("SSR_ITEM").d("Original item: $item")

                                val wrappedData = mapOf("item" to item)
                                Timber.tag("SSR_ITEM").d("Wrapped data: $wrappedData")

                                val boundTemplate =
                                    bindDataToTemplate(itemTemplate.layout, wrappedData)
                                val itemComponent = createComponent(boundTemplate)

                                val clickHandler =
                                    itemTemplate.actions?.get("onClick")?.let { action ->
                                        val boundAction = bindDataToAction(action, wrappedData)
                                        return@let {
                                            Timber.tag("SSR_ACTION")
                                                .d("Item clicked: ${boundAction.destination}")
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
    private fun CreateEnhancedLazyColumn(node: ComponentNode) {
        val dataSource = node.dataSource
        val itemTemplate = node.itemTemplate

        if (dataSource != null && itemTemplate != null) {
            when (dataSource.type) {
                "static" -> {
                    val items = dataSource.items ?: emptyList()
                    Timber.tag("SSR_ENHANCED_LAZY")
                        .d("Creating enhanced LazyColumn with ${items.size} items")

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
                            items(items.size) { index ->
                                val item = items[index]

                                val templateToUse = when {
                                    item is Map<*, *> && item.containsKey("template") -> {
                                        // Parse custom template for this item
                                        parseComponentNode(item["template"])
                                    }

                                    item is Map<*, *> && item.containsKey("component_type") -> {
                                        // Create template based on component type
                                        createTemplateForType(
                                            item["component_type"] as? String ?: "default", item
                                        )
                                    }

                                    else -> itemTemplate.layout
                                }

                                val wrappedData = when (item) {
                                    is Map<*, *> -> item as Map<String, Any>
                                    else -> mapOf("item" to item)
                                }

                                val boundTemplate = bindDataToTemplate(templateToUse, wrappedData)
                                val itemComponent = createComponent(boundTemplate)

                                val clickHandler =
                                    itemTemplate.actions?.get("onClick")?.let { action ->
                                        val boundAction = bindDataToAction(action, wrappedData)
                                        return@let {
                                            Timber.tag("SSR_ENHANCED_ACTION")
                                                .d("Item clicked: ${boundAction.destination}")
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
                    Text("API LazyColumn not implemented in enhanced version")
                }

                else -> {
                    Text("Unsupported data source type: ${dataSource.type}")
                }
            }
        } else {
            Text("Enhanced LazyColumn requires dataSource and itemTemplate")
        }
    }

    private fun createTemplateForType(type: String, itemData: Map<String, Any>): ComponentNode {
        return when (type) {
            "chart_bar" -> ComponentNode(
                type = "card",
                children = listOf(
                    ComponentNode(
                        type = "bar_chart",
                        properties = mapOf(
                            "title" to (itemData["title"] ?: "Chart"),
                            "subtitle" to (itemData["subtitle"] ?: ""),
                            "showLegend" to true,
                            "showGrid" to true,
                            "showValues" to true,
                            "height" to 350,
                            "data" to (itemData["data"] ?: emptyList<Any>())
                        )
                    )
                ),
                modifier = ModifierConfig(fillMaxWidth = true, padding = 8)
            )

            "chart_line" -> ComponentNode(
                type = "card",
                children = listOf(
                    ComponentNode(
                        type = "line_chart",
                        properties = mapOf(
                            "title" to (itemData["title"] ?: "Chart"),
                            "subtitle" to (itemData["subtitle"] ?: ""),
                            "showLegend" to true,
                            "showGrid" to true,
                            "height" to 350,
                            "series" to (itemData["series"] ?: emptyList<Any>())
                        )
                    )
                ),
                modifier = ModifierConfig(fillMaxWidth = true, padding = 8)
            )

            "chart_pie" -> ComponentNode(
                type = "card",
                children = listOf(
                    ComponentNode(
                        type = "pie_chart",
                        properties = mapOf(
                            "title" to (itemData["title"] ?: "Chart"),
                            "subtitle" to (itemData["subtitle"] ?: ""),
                            "showLegend" to true,
                            "showValues" to true,
                            "height" to 350,
                            "data" to (itemData["data"] ?: emptyList<Any>())
                        )
                    )
                ),
                modifier = ModifierConfig(fillMaxWidth = true, padding = 8)
            )

            "chart_radar" -> ComponentNode(
                type = "card",
                children = listOf(
                    ComponentNode(
                        type = "radar_chart",
                        properties = mapOf(
                            "title" to (itemData["title"] ?: "Chart"),
                            "subtitle" to (itemData["subtitle"] ?: ""),
                            "showLabels" to true,
                            "height" to 350,
                            "data" to (itemData["data"] ?: emptyList<Any>())
                        )
                    )
                ),
                modifier = ModifierConfig(fillMaxWidth = true, padding = 8)
            )

            "spacer" -> ComponentNode(
                type = "spacer",
                properties = mapOf(
                    "height" to (itemData["height"] ?: 16)
                )
            )

            else -> ComponentNode(
                type = "text",
                properties = mapOf("text" to "Unknown item type: $type")
            )
        }
    }

    private fun parseComponentNode(template: Any?): ComponentNode {
        return when (template) {
            is Map<*, *> -> {
                val map = template as Map<String, Any>
                ComponentNode(
                    type = map["type"] as? String ?: "text",
                    id = map["id"] as? String,
                    properties = map["properties"] as? Map<String, Any>,
                    modifier = parseModifierConfig(map["modifier"]),
                    children = (map["children"] as? List<*>)?.mapNotNull {
                        parseComponentNode(it)
                    },
                    actions = map["actions"] as? Map<String, ActionConfig>
                )
            }

            else -> ComponentNode(type = "text", properties = mapOf("text" to "Invalid template"))
        }
    }

    private fun parseModifierConfig(modifier: Any?): ModifierConfig? {
        return when (modifier) {
            is Map<*, *> -> {
                val map = modifier as Map<String, Any>
                ModifierConfig(
                    padding = map["padding"] as? Int,
                    fillMaxSize = map["fillMaxSize"] as? Boolean,
                    fillMaxWidth = map["fillMaxWidth"] as? Boolean,
                    width = map["width"] as? Int,
                    height = map["height"] as? Int,
                    weight = (map["weight"] as? Number)?.toFloat()
                )
            }

            else -> null
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

    @Composable
    private fun CreateBarChart(node: ComponentNode) {
        val props = node.properties ?: emptyMap()

        // Parse chart data
        val chartData = parseChartData(props["data"])
        val config = parseChartConfig(props)

        Timber.tag("SSR_CHART").d("Creating BarChart with ${chartData.size} data points")

        BarChart(
            data = chartData,
            config = config,
            modifier = createModifier(node.modifier)
        )
    }

    @Composable
    private fun CreateLineChart(node: ComponentNode) {
        val props = node.properties ?: emptyMap()
        val series = parseSeriesData(props["series"])
        val config = parseChartConfig(props)

        Timber.tag("SSR_CHART").d("Creating LineChart with ${series.size} series")

        LineChart(
            series = series,
            config = config,
            modifier = createModifier(node.modifier),
            props
        )
    }

    @Composable
    private fun CreatePieChart(node: ComponentNode) {
        val props = node.properties ?: emptyMap()

        val chartData = parseChartData(props["data"])
        val config = parseChartConfig(props)

        Timber.tag("SSR_CHART").d("Creating PieChart with ${chartData.size} data points")

        PieChart(
            data = chartData,
            config = config,
            modifier = createModifier(node.modifier)
        )
    }

    @Composable
    private fun CreateBubbleChart(node: ComponentNode) {
        val props = node.properties ?: emptyMap()

        val chartData = parseChartData(props["data"])
        val config = parseChartConfig(props)

        Timber.tag("SSR_CHART").d("Creating BubbleChart with ${chartData.size} data points")

        BubbleChart(
            data = chartData,
            config = config,
            modifier = createModifier(node.modifier)
        )
    }

    @Composable
    private fun CreateRadarChart(node: ComponentNode) {
        val props = node.properties ?: emptyMap()

        val chartData = parseChartData(props["data"])
        val config = parseChartConfig(props)

        Timber.tag("SSR_CHART").d("Creating RadarChart with ${chartData.size} data points")

        RadarChart(
            data = chartData,
            config = config,
            modifier = createModifier(node.modifier)
        )
    }

    private fun parseChartData(dataProperty: Any?): List<ChartDataPoint> {
        return when (dataProperty) {
            is List<*> -> {
                dataProperty.mapNotNull { item ->
                    when (item) {
                        is Map<*, *> -> {
                            val label = item["label"] as? String ?: ""
                            val color = item["color"] as? String
                            val metadata = item["metadata"] as? Map<String, Any>
                            val value = when (val v = item["value"]) {
                                is Number -> v.toDouble()
                                is String -> v.toDoubleOrNull() ?: 0.0
                                else -> null
                            }
                            if (value == null) {
                                return@mapNotNull null
                            }
                            ChartDataPoint(
                                label = label,
                                value = value,
                                color = color,
                                metadata = metadata
                            )
                        }

                        else -> null
                    }
                }
            }

            else -> emptyList()
        }
    }

    private fun parseSeriesData(seriesProperty: Any?): List<ChartSeries> {
        return when (seriesProperty) {
            is List<*> -> {
                seriesProperty.mapNotNull { item ->
                    when (item) {
                        is Map<*, *> -> {
                            val name = item["name"] as? String ?: ""
                            val color = item["color"] as? String
                            val data = parseChartData(item["data"])

                            ChartSeries(
                                name = name,
                                data = data,
                                color = color
                            )
                        }

                        else -> null
                    }
                }
            }

            else -> emptyList()
        }
    }

    private fun parseChartConfig(props: Map<String, Any>): ChartConfig {
        return ChartConfig(
            title = props["title"] as? String,
            subtitle = props["subtitle"] as? String,
            showLegend = props["showLegend"] as? Boolean ?: true,
            showGrid = props["showGrid"] as? Boolean ?: true,
            showLabels = props["showLabels"] as? Boolean ?: true,
            showValues = props["showValues"] as? Boolean ?: false,
            animated = props["animated"] as? Boolean ?: true,
            colors = (props["colors"] as? List<*>)?.filterIsInstance<String>(),
            height = (props["height"] as? Number)?.toInt(),
            width = (props["width"] as? Number)?.toInt()
        )
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