package com.cincinnatiai.ssr_library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.cincinnatiai.ssr_library.componentfactory.ComponentPreparationResult
import com.cincinnatiai.ssr_library.componentfactory.DefaultComponentFactory
import com.cincinnatiai.ssr_library.model.CoilImageLoader
import com.cincinnatiai.ssr_library.model.CompiledModifier
import com.cincinnatiai.ssr_library.model.ComponentMetadata
import com.cincinnatiai.ssr_library.model.ComponentNode
import com.cincinnatiai.ssr_library.model.ComponentRenderState
import com.cincinnatiai.ssr_library.model.ComponentScreen
import com.cincinnatiai.ssr_library.model.ComponentType
import com.cincinnatiai.ssr_library.model.ComposableTree
import com.cincinnatiai.ssr_library.model.ImageLoader
import com.cincinnatiai.ssr_library.model.ModifierConfig
import com.cincinnatiai.ssr_library.model.PrecompiledComponent
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap

interface ServerSideRenderer {
    @Composable
    fun RenderScreen(
        json: String,
        loadingContent: @Composable () -> Unit,
        errorContent: @Composable (String, () -> Unit) -> Unit
    )

    fun clearCache()
    fun getCacheSize(): Int
}

interface ComponentFactory {
    suspend fun prepareComponent(node: ComponentNode): ComponentPreparationResult
    fun createComponent(node: ComponentNode): @Composable () -> Unit
}

class ServerSideRendererImpl @OptIn(ExperimentalCoroutinesApi::class) constructor(
    private val gson: Gson = Gson(),
    private val imageLoader: ImageLoader = CoilImageLoader(),
    private val componentFactory: ComponentFactory = DefaultComponentFactory(imageLoader = imageLoader),
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val parsingDispatcher: CoroutineDispatcher = Dispatchers.Default.limitedParallelism(2)
) : ServerSideRenderer {

    private val componentCache = ConcurrentHashMap<String, ComposableTree>()

    init {
        if (SSRLogging.isEnabled) {
            Timber.d("🚀 ServerSideRendererImpl initialized")
            Timber.d("ComponentFactory: ${componentFactory::class.simpleName}")
            Timber.d("ImageLoader: ${imageLoader::class.simpleName}")
        }
    }

    @Composable
    override fun RenderScreen(
        json: String,
        loadingContent: @Composable () -> Unit,
        errorContent: @Composable (String, () -> Unit) -> Unit
    ) {
        if (SSRLogging.isEnabled) {
            Timber.d("RenderScreen called with JSON length: ${json.length}")
        }

        var renderState by remember { mutableStateOf<ComponentRenderState>(ComponentRenderState.Loading) }
        val cacheKey = remember(json) {
            val key = json.hashCode().toString()
            if (SSRLogging.isEnabled) {
                Timber.d("Generated cache key: $key")
            }
            key
        }

        LaunchedEffect(json) {
            if (SSRLogging.isEnabled) {
                Timber.d("LaunchedEffect started for cache key: $cacheKey")
            }
            renderState = ComponentRenderState.Loading

            try {
                val cached = componentCache[cacheKey]
                if (cached != null) {
                    if (SSRLogging.isEnabled) {
                        Timber.d("Cache HIT for key: $cacheKey")
                    }
                    renderState = ComponentRenderState.Success(cached)
                } else {
                    if (SSRLogging.isEnabled) {
                        Timber.d("Cache MISS for key: $cacheKey")
                        Timber.d("Building component tree...")
                    }

                    val startTime = System.currentTimeMillis()
                    val composableTree = withContext(parsingDispatcher) {
                        buildComponentTree(json)
                    }
                    val buildTime = System.currentTimeMillis() - startTime

                    if (SSRLogging.isEnabled) {
                        Timber.d("Component tree built in ${buildTime}ms")
                    }
                    componentCache[cacheKey] = composableTree
                    if (SSRLogging.isEnabled) {
                        Timber.d("Component tree cached. Cache size: ${componentCache.size}")
                    }

                    renderState = ComponentRenderState.Success(composableTree)
                }
            } catch (e: Throwable) {

                Timber.e(e, "ERROR in RenderScreen")
                if (SSRLogging.isEnabled) {
                    Timber.e("JSON that caused error: $json")
                }
                renderState = ComponentRenderState.Error(
                    message = "Failed to Render ComponentRenderState: ${e.message}",
                    throwable = e
                )
            }
        }

        when (val state = renderState) {
            is ComponentRenderState.Error -> {

                Timber.e("Rendering error state: ${state.message}")
                errorContent(state.message) {
                    if (SSRLogging.isEnabled) {
                        Timber.d("Retry triggered")
                    }
                    renderState = ComponentRenderState.Loading
                }
            }

            ComponentRenderState.Loading -> {
                if (SSRLogging.isEnabled) {
                    Timber.d("Rendering loading state")
                }
                loadingContent()
            }

            is ComponentRenderState.Success -> {
                if (SSRLogging.isEnabled) {
                    Timber.d("Rendering success state")
                }

                state.composableTree.rootComponent()
            }
        }
    }

    private suspend fun buildComponentTree(json: String): ComposableTree =
        withContext(parsingDispatcher) {
            if (SSRLogging.isEnabled) {
                Timber.d("Building component tree...")
            }

            try {
                if (SSRLogging.isEnabled) {
                    Timber.d("Step 1: Parsing JSON...")
                }
                val componentScreen = parseComponentJson(json)
                if (SSRLogging.isEnabled) {
                    Timber.d("JSON parsed successfully")
                    Timber.d("Screen ID: ${componentScreen.screen.id}")
                    Timber.d("Screen Title: ${componentScreen.screen.title}")
                    Timber.d("Root component type: ${componentScreen.screen.layout.type}")
                }

                if (SSRLogging.isEnabled) {
                    Timber.d("Step 2: Analyzing component tree...")
                }
                val metadata = analyzeComponentTree(componentScreen.screen.layout)
                if (SSRLogging.isEnabled) {
                    Timber.d("Analysis complete - Components: ${metadata.componentCount}, Max depth: ${metadata.maxDepth}")
                }

                if (SSRLogging.isEnabled) {
                    Timber.d("Step 3: Precompiling components...")
                }
                val precompiledComponents =
                    precompileComponentsWithFactory(componentScreen.screen.layout)
                if (SSRLogging.isEnabled) {
                    Timber.d("Components precompiled successfully")
                }

                if (SSRLogging.isEnabled) {
                    Timber.d("Step 4: Building root component...")
                }
                val rootComponent: @Composable () -> Unit = {
                    if (SSRLogging.isEnabled) {
                        Timber.d("Rendering root component...")
                    }
                    // TODO implement and apply custom themes
                    val theme = componentScreen.theme
                    if (theme != null && SSRLogging.isEnabled) {
                        Timber.d("Theme found: primary=${theme.primaryColor}, background=${theme.backgroundColor}")
                    } else if (SSRLogging.isEnabled) {
                        Timber.d("No theme provided, using default")
                    }
                    RenderPrecompiledComponent(precompiledComponents)
                }

                val tree = ComposableTree(rootComponent, metadata)
                if (SSRLogging.isEnabled) {
                    Timber.d("Component tree built successfully")
                }
                return@withContext tree

            } catch (e: Exception) {
                Timber.e(e, "Error building component tree")
                if (SSRLogging.isEnabled) {
                    Timber.e("JSON content: $json")
                }
                throw e
            }
        }

    private suspend fun precompileComponentsWithFactory(node: ComponentNode): PrecompiledComponent =
        withContext(dispatcher) {
            if (SSRLogging.isEnabled) {
                Timber.d("Precompiling component: ${node.type} (id: ${node.id})")
            }

            try {
                val result = componentFactory.prepareComponent(node)
                val componentType = ComponentType.fromString(node.type)
                val modifier = precompileModifier(node.modifier)
                val properties = node.properties?.toMap() ?: emptyMap()

                when (result) {
                    is ComponentPreparationResult.Error -> {
                        Timber.w("Component preparation failed for ${node.type}: ${result.message}")
                        return@withContext PrecompiledComponent(
                            type = componentType,
                            properties = properties,
                            modifier = modifier,
                            children = emptyList(), // No children for error case
                            dataSource = node.dataSource,
                            itemTemplate = node.itemTemplate,
                            actions = node.actions,
                            factoryComponent = result.fallbackComponent
                                ?: componentFactory.createComponent(node)
                        )
                    }

                    is ComponentPreparationResult.Success -> {
                        if (SSRLogging.isEnabled) {
                            Timber.d("Component preparation successful for ${node.type}")
                        }

                        val children = node.children?.mapIndexed { index, child ->
                            if (SSRLogging.isEnabled) {
                                Timber.d("Processing child $index of ${node.type}: ${child.type}")
                            }
                            precompileComponentsWithFactory(child) // Fixed: was 'node' before!
                        } ?: emptyList()

                        if (SSRLogging.isEnabled) {
                            Timber.d("Processed ${children.size} children for ${node.type}")
                        }

                        return@withContext PrecompiledComponent(
                            type = componentType,
                            properties = properties,
                            modifier = modifier,
                            children = children,
                            dataSource = node.dataSource,
                            itemTemplate = node.itemTemplate,
                            actions = node.actions,
                            factoryComponent = result.component
                        )
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Error precompiling component ${node.type}")
                // Return fallback component
                return@withContext PrecompiledComponent(
                    type = ComponentType.fromString(node.type),
                    properties = node.properties?.toMap() ?: emptyMap(),
                    modifier = precompileModifier(node.modifier),
                    children = emptyList(),
                    dataSource = node.dataSource,
                    itemTemplate = node.itemTemplate,
                    actions = node.actions,
                    factoryComponent = {
                        androidx.compose.material3.Text("Error: ${node.type}")
                    }
                )
            }
        }

    override fun clearCache() {
        if (SSRLogging.isEnabled) {
            Timber.d("Clearing cache (was ${componentCache.size} items)")
        }
        componentCache.clear()
    }

    override fun getCacheSize(): Int {
        val size = componentCache.size
        if (SSRLogging.isEnabled) {
            Timber.d("Cache size: $size")
        }
        return size
    }

    @Composable
    private fun RenderPrecompiledComponent(precompiled: PrecompiledComponent) {
        if (SSRLogging.isEnabled) {
            Timber.d("Rendering precompiled component: ${precompiled.type}")
        }
        precompiled.factoryComponent()
    }

    private suspend fun parseComponentJson(json: String): ComponentScreen {
        return withContext(dispatcher) {
            if (SSRLogging.isEnabled) {
                Timber.d("Parsing JSON...")
            }
            try {
                val result = gson.fromJson(json, ComponentScreen::class.java)
                if (SSRLogging.isEnabled) {
                    Timber.d("JSON parsing successful")
                }

                // Validate required fields
                if (result.screen?.layout == null) {
                    throw IllegalArgumentException("Missing required field: screen.layout")
                }

                if (SSRLogging.isEnabled) {
                    Timber.d("JSON validation passed")
                }
                return@withContext result

            } catch (e: Exception) {
                Timber.e(e, "💥 JSON parsing failed")
                if (SSRLogging.isEnabled) {
                    Timber.e("Invalid JSON: $json")
                }
                throw e
            }
        }
    }

    private suspend fun analyzeComponentTree(node: ComponentNode): ComponentMetadata {
        return withContext(dispatcher) {
            if (SSRLogging.isEnabled) {
                Timber.d("Analyzing component tree...")
            }
            var componentCount = 0
            var maxDepth = 0
            var hasAsyncComponents = false

            fun analyzeNode(node: ComponentNode, depth: Int) {
                componentCount++
                maxDepth = maxOf(maxDepth, depth)

                if (SSRLogging.isEnabled && SSRLogging.isVerboseEnabled) {
                    Timber.v("Analyzing: ${node.type} at depth $depth")
                }

                if (node.type in listOf("lazy_column", "lazy_row", "lazy_grid") &&
                    node.dataSource?.type == "api"
                ) {
                    hasAsyncComponents = true
                    if (SSRLogging.isEnabled) {
                        Timber.d("Found async component: ${node.type}")
                    }
                }

                node.children?.forEachIndexed { index, child ->
                    if (SSRLogging.isEnabled && SSRLogging.isVerboseEnabled) {
                        Timber.v("Child $index: ${child.type}")
                    }
                    analyzeNode(child, depth + 1)
                }
            }

            analyzeNode(node, 0)

            val metadata = ComponentMetadata(
                componentCount = componentCount,
                maxDepth = maxDepth,
                hasAsyncComponents = hasAsyncComponents,
                estimatedRenderTime = componentCount * 2L // rough estimate in ms
            )

            if (SSRLogging.isEnabled) {
                Timber.d("Analysis complete: $componentCount components, depth $maxDepth, async: $hasAsyncComponents")
            }
            return@withContext metadata
        }
    }

    private fun precompileModifier(modifierConfig: ModifierConfig?): CompiledModifier {
        if (SSRLogging.isEnabled && SSRLogging.isVerboseEnabled) {
            Timber.v("Precompiling modifier: $modifierConfig")
        }
        return CompiledModifier(
            padding = modifierConfig?.padding,
            fillMaxSize = modifierConfig?.fillMaxSize ?: false,
            fillMaxWidth = modifierConfig?.fillMaxWidth ?: false,
            width = modifierConfig?.width,
            height = modifierConfig?.height,
            weight = modifierConfig?.weight
        )
    }
}

object SSRLogging {
    var isEnabled = false
    var isVerboseEnabled = false

    fun enable() {
        isEnabled = true
        Timber.d("SSR logging enabled")
    }

    fun disable() {
        isEnabled = false
        Timber.d("SSR logging disabled")
    }

    fun enableVerbose() {
        isEnabled = true
        isVerboseEnabled = true
        Timber.d("Verbose logging enabled")
    }

    fun disableVerbose() {
        isVerboseEnabled = false
        Timber.d("Verbose logging disabled")
    }
}