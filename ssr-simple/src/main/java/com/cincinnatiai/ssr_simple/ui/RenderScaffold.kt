@file:OptIn(ExperimentalMaterial3Api::class)

package com.cincinnatiai.ssr_simple.ui

import android.graphics.RenderNode
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cincinnatiai.ssr_simple.model.NodeModel
import androidx.core.graphics.toColorInt
import coil.compose.AsyncImage
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.ui.layout.ContentScale

@Composable
fun RenderScaffold(node: NodeModel, onAction: (String) -> Unit) {
    Scaffold(
        modifier = buildModifier(node.modifier),
        topBar = { node.topBar?.let { RenderNode(it, onAction) } },
        floatingActionButton = {
            node.floatingActionButton?.let { RenderNode(it, onAction) }
        }
    ) { innerPadding ->
        node.content?.let {
            Box(modifier = Modifier.padding(innerPadding)) {
                RenderNode(it, onAction)
            }
        }
    }
}

@Composable
fun RenderTopBar(node: NodeModel) {
    TopAppBar(title = { Text(node.title ?: "") })
}

@Composable
fun RenderColumn(node: NodeModel, onAction: (String) -> Unit) {
    val horizontalAlignment = when (node.modifier?.horizontalAlignment?.lowercase()) {
        "start" -> Alignment.Start
        "center" -> Alignment.CenterHorizontally
        "end" -> Alignment.End
        else -> Alignment.Start
    }

    Column(
        modifier = buildModifier(node.modifier),
        horizontalAlignment = horizontalAlignment
    ) {
        node.children?.forEach { RenderNode(it, onAction) }
    }
}

@Composable
fun RenderRow(node: NodeModel, onAction: (String) -> Unit) {
    val verticalAlignment = when (node.modifier?.verticalAlignment?.lowercase()) {
        "top" -> Alignment.Top
        "center" -> Alignment.CenterVertically
        "bottom" -> Alignment.Bottom
        else -> Alignment.Top
    }

    Row(
        modifier = buildModifier(node.modifier),
        verticalAlignment = verticalAlignment
    ) {
        node.children?.forEach { RenderNode(it, onAction) }
    }
}

@Composable
fun RenderBox(node: NodeModel, onAction: (String) -> Unit) {
    val contentAlignment = when (node.modifier?.contentAlignment?.lowercase()) {
        "topstart" -> Alignment.TopStart
        "topcenter" -> Alignment.TopCenter
        "topend" -> Alignment.TopEnd
        "centerstart" -> Alignment.CenterStart
        "center" -> Alignment.Center
        "centerend" -> Alignment.CenterEnd
        "bottomstart" -> Alignment.BottomStart
        "bottomcenter" -> Alignment.BottomCenter
        "bottomend" -> Alignment.BottomEnd
        else -> Alignment.TopStart
    }

    Box(
        modifier = buildModifier(node.modifier),
        contentAlignment = contentAlignment
    ) {
        node.children?.forEach { RenderNode(it, onAction) }
    }
}

@Composable
fun RenderLazyColumn(node: NodeModel, onAction: (String) -> Unit) {
    LazyColumn(modifier = buildModifier(node.modifier)) {
        node.children?.let { items(it) { child -> RenderNode(child, onAction) } }
    }
}

@Composable
fun RenderCard(node: NodeModel, onAction: (String) -> Unit = {}) {
    val bgColor = node.backgroundColor?.let {
        Color(android.graphics.Color.parseColor(it))
    } ?: MaterialTheme.colorScheme.surface

    val elevation = node.elevation?.dp ?: 4.dp

    Card(
        modifier = buildModifier(node.modifier),
        colors = CardDefaults.cardColors(bgColor),
        elevation = CardDefaults.cardElevation(elevation)
    ) {
        if (node.children != null) {
            // Render custom children without default padding
            node.children.forEach { RenderNode(it, onAction) }
        } else {
            // Render default title/description with padding
            Column(modifier = Modifier.padding(16.dp)) {
                node.title?.let { Text(it, style = MaterialTheme.typography.titleMedium) }
                node.description?.let {
                    Text(it, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 8.dp))
                }
            }
        }
    }
}

@Composable
fun RenderText(node: NodeModel) {
    val style = buildTextStyle(node.textStyle)
    Text(
        text = node.title ?: node.description ?: node.label ?: "",
        modifier = buildModifier(node.modifier),
        style = style,
        textAlign = style.textAlign
    )
}

@Composable
fun RenderButton(node: NodeModel, onAction: (String) -> Unit) {
    val bgColor = node.backgroundColor?.let {
        Color(it.toColorInt())
    } ?: MaterialTheme.colorScheme.primary

    Button(
        onClick = { node.action?.let { onAction(it) } },
        modifier = buildModifier(node.modifier),
        colors = ButtonDefaults.buttonColors(containerColor = bgColor)
    ) {
        Text(node.label ?: "Button")
    }
}

@Composable
fun RenderImage(node: NodeModel) {
    var modifier = buildModifier(node.modifier)

    // Apply size constraints if provided
    if (node.imageWidth != null && node.imageHeight != null) {
        modifier = modifier.size(width = node.imageWidth.dp, height = node.imageHeight.dp)
    } else if (node.imageWidth != null) {
        modifier = modifier.width(node.imageWidth.dp)
    } else if (node.imageHeight != null) {
        modifier = modifier.height(node.imageHeight.dp)
    }

    AsyncImage(
        model = node.imageUrl,
        contentDescription = node.contentDescription ?: node.title ?: "Image",
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}