package com.cincinnatiai.ssr_simple.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.cincinnatiai.ssr_simple.model.NodeModel

@Composable
fun RenderNode(node: NodeModel, onAction: (String) -> Unit = {}) {
    when (node.type) {
        "Scaffold" -> RenderScaffold(node, onAction)
        "TopAppBar" -> RenderTopBar(node)
        "Column" -> RenderColumn(node, onAction)
        "Row" -> RenderRow(node, onAction)
        "Box" -> RenderBox(node, onAction)
        "LazyColumn" -> RenderLazyColumn(node, onAction)
        "Card" -> RenderCard(node, onAction)
        "Text" -> RenderText(node)
        "Button" -> RenderButton(node, onAction)
        "Image" -> RenderImage(node)
        "Table" -> RenderTable(node, onAction)
        "Spacer" -> RenderSpacer(node)
        "VideoItem" -> RenderVideoItem(node, onAction)
        "HorizontalPager" -> RenderHorizontalPager(node, onAction)
        else -> Text("Unknown type: ${node.type}")
    }
}