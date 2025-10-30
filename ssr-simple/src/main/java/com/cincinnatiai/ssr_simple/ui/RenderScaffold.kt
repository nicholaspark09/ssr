@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.cincinnatiai.ssr_simple.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import coil.compose.AsyncImage
import com.cincinnatiai.ssr_simple.model.NodeModel
import com.cincinnatiai.ssr_simple.model.TableCellModel
import com.cincinnatiai.ssr_simple.model.TableColumnModel

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

    val baseModifier = buildModifier(node.modifier)
    val scrollModifier = if (node.modifier?.verticalScroll == true) {
        baseModifier.verticalScroll(rememberScrollState())
    } else {
        baseModifier
    }

    Column(
        modifier = scrollModifier,
        horizontalAlignment = horizontalAlignment
    ) {
        node.children?.forEach { child ->
            if (child.modifier?.weight != null) {
                Box(modifier = Modifier.weight(child.modifier.weight)) {
                    RenderNode(child, onAction)
                }
            } else {
                RenderNode(child, onAction)
            }
        }
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
        node.children?.forEach { child ->
            if (child.modifier?.weight != null) {
                Box(modifier = Modifier.weight(child.modifier.weight)) {
                    RenderNode(child, onAction)
                }
            } else {
                RenderNode(child, onAction)
            }
        }
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
        when {
            node.content != null -> {
                RenderNode(node.content, onAction)
            }

            node.children != null -> {
                node.children.forEach { RenderNode(it, onAction) }
            }

            else -> {
                Column(modifier = Modifier.padding(16.dp)) {
                    node.title?.let { Text(it, style = MaterialTheme.typography.titleMedium) }
                    node.description?.let {
                        Text(
                            it,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RenderVideoItem(node: NodeModel, onAction: (String) -> Unit = {}) {
    val bgColor = node.backgroundColor?.let {
        Color(it.toColorInt())
    } ?: MaterialTheme.colorScheme.surface

    val elevation = node.elevation?.dp ?: 4.dp

    val modifier = buildModifier(node.modifier).apply {
        node.action?.let { it ->
            clickable {
                onAction(it)
            }
        }
    }
    var shape = CardDefaults.shape
    node.roundedCorners?.let { it ->
        shape = RoundedCornerShape(it)
    }
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(bgColor),
        elevation = CardDefaults.cardElevation(elevation),
        shape = shape
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier
                .padding(16.dp)
                .weight(1f)) {
                node.title?.let { Text(it, style = MaterialTheme.typography.titleMedium) }
                node.description?.let {
                    Text(
                        it,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
            node.imageUrl?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = node.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
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
    val variant = node.buttonVariant?.lowercase() ?: "filled"
    val bgColor = node.backgroundColor?.let {
        Color(it.toColorInt())
    } ?: MaterialTheme.colorScheme.primary

    when (variant) {
        "outlined" -> {
            OutlinedButton(
                onClick = { node.action?.let { onAction(it) } },
                modifier = buildModifier(node.modifier),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = bgColor
                )
            ) {
                Text(node.label ?: "Button")
            }
        }

        "text" -> {
            TextButton(
                onClick = { node.action?.let { onAction(it) } },
                modifier = buildModifier(node.modifier),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = bgColor
                )
            ) {
                Text(node.label ?: "Button")
            }
        }

        else -> { // "filled" or default
            Button(
                onClick = { node.action?.let { onAction(it) } },
                modifier = buildModifier(node.modifier),
                colors = ButtonDefaults.buttonColors(containerColor = bgColor)
            ) {
                Text(node.label ?: "Button")
            }
        }
    }
}

@Composable
fun RenderSpacer(node: NodeModel) {
    Spacer(modifier = buildModifier(node.modifier))
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

@Composable
fun RenderTable(node: NodeModel, onAction: (String) -> Unit) {
    val columns = node.columns ?: return
    val tableData = node.tableData ?: emptyList()
    val showBorders = node.showBorders ?: false
    val useLazyColumn = node.useLazyColumn ?: true
    val headerBgColor = node.headerBackgroundColor?.let {
        Color(it.toColorInt())
    } ?: MaterialTheme.colorScheme.primaryContainer

    val roundedCorners = node.roundedCorners?.dp ?: 0.dp
    val hasRoundedCorners = roundedCorners > 0.dp

    val tableModifier = buildModifier(node.modifier)
        .then(if (hasRoundedCorners) Modifier.clip(RoundedCornerShape(roundedCorners)) else Modifier)
        .then(
            if (showBorders) Modifier.border(
                1.dp,
                Color.Gray,
                RoundedCornerShape(roundedCorners)
            ) else Modifier
        )

    Column(modifier = tableModifier) {
        // Header Row
        val headerBorderModifier = if (showBorders) {
            Modifier.drawBehind {
                // Draw bottom border for header row
                drawLine(
                    color = Color.Gray,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 1.dp.toPx()
                )
            }
        } else {
            Modifier
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .background(headerBgColor)
                .then(headerBorderModifier)
        ) {
            columns.forEachIndexed { index, column ->
                TableHeaderCell(
                    column = column,
                    showBorders = showBorders,
                    isLastColumn = index == columns.size - 1
                )
            }
        }

        // Data Rows - Use LazyColumn or regular Column based on useLazyColumn property
        if (useLazyColumn) {
            LazyColumn {
                itemsIndexed(tableData) { rowIndex, rowData ->
                    TableDataRow(
                        rowData = rowData,
                        columns = columns,
                        showBorders = showBorders,
                        rowAction = node.rowAction,
                        onAction = onAction
                    )
                }
            }
        } else {
            tableData.forEach { rowData ->
                TableDataRow(
                    rowData = rowData,
                    columns = columns,
                    showBorders = showBorders,
                    rowAction = node.rowAction,
                    onAction = onAction
                )
            }
        }
    }
}

@Composable
private fun TableDataRow(
    rowData: List<TableCellModel>,
    columns: List<TableColumnModel>,
    showBorders: Boolean,
    rowAction: String?,
    onAction: (String) -> Unit,
) {
    val rowModifier = if (rowAction != null) {
        Modifier.clickable { onAction(rowAction) }
    } else {
        Modifier
    }

    Row(
        modifier = rowModifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        rowData.forEachIndexed { cellIndex, cell ->
            if (cellIndex < columns.size) {
                TableDataCell(
                    cell = cell,
                    column = columns[cellIndex],
                    showBorders = showBorders,
                    isLastColumn = cellIndex == columns.size - 1,
                    onAction = onAction
                )
            }
        }
    }
}

@Composable
private fun RowScope.TableHeaderCell(
    column: TableColumnModel,
    showBorders: Boolean,
    isLastColumn: Boolean,
) {
    val weight = column.weight ?: 1f
    val alignment = when (column.horizontalAlignment?.lowercase()) {
        "start" -> TextAlign.Start
        "center" -> TextAlign.Center
        "end" -> TextAlign.End
        else -> TextAlign.Start
    }

    val textStyle = buildTextStyle(column.headerStyle).copy(
        textAlign = alignment,
        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
    )

    val borderColor = Color.Gray
    val borderModifier = if (showBorders && !isLastColumn) {
        Modifier.drawBehind {
            // Draw right border only
            drawLine(
                color = borderColor,
                start = Offset(size.width, 0f),
                end = Offset(size.width, size.height),
                strokeWidth = 1.dp.toPx()
            )
        }
    } else {
        Modifier
    }

    Box(
        modifier = Modifier
            .weight(weight)
            .fillMaxHeight()
            .then(
                if (column.width != null) {
                    Modifier.width(column.width.dp)
                } else {
                    Modifier
                }
            )
            .then(borderModifier)
            .padding(8.dp),
        contentAlignment = when (alignment) {
            TextAlign.Start -> Alignment.CenterStart
            TextAlign.Center -> Alignment.Center
            TextAlign.End -> Alignment.CenterEnd
            else -> Alignment.CenterStart
        }
    ) {
        Text(
            text = column.header,
            style = textStyle
        )
    }
}

@Composable
private fun RowScope.TableDataCell(
    cell: TableCellModel,
    column: TableColumnModel,
    showBorders: Boolean,
    isLastColumn: Boolean,
    onAction: (String) -> Unit,
) {
    val weight = column.weight ?: 1f
    val alignment = when (column.horizontalAlignment?.lowercase()) {
        "start" -> TextAlign.Start
        "center" -> TextAlign.Center
        "end" -> TextAlign.End
        else -> TextAlign.Start
    }

    // Cell style can override column style
    val textStyle = buildTextStyle(cell.textStyle ?: column.textStyle).copy(
        textAlign = alignment
    )

    val cellBgColor = cell.backgroundColor?.let {
        Color(it.toColorInt())
    }

    // Cell can override table's showBorders setting
    val cellShowBorders = cell.showBorder ?: showBorders

    val borderColor = Color.Gray
    val borderModifier = if (cellShowBorders) {
        Modifier.drawBehind {
            // Draw bottom border
            drawLine(
                color = borderColor,
                start = Offset(0f, size.height),
                end = Offset(size.width, size.height),
                strokeWidth = 1.dp.toPx()
            )
            // Draw right border only if not last column
            if (!isLastColumn) {
                drawLine(
                    color = borderColor,
                    start = Offset(size.width, 0f),
                    end = Offset(size.width, size.height),
                    strokeWidth = 1.dp.toPx()
                )
            }
        }
    } else {
        Modifier
    }

    Box(
        modifier = Modifier
            .weight(weight)
            .fillMaxHeight()
            .then(
                if (column.width != null) {
                    Modifier.width(column.width.dp)
                } else {
                    Modifier
                }
            )
            .then(if (cellBgColor != null) Modifier.background(cellBgColor) else Modifier)
            .then(borderModifier)
            .then(
                if (cell.action != null) {
                    Modifier.clickable { onAction(cell.action) }
                } else {
                    Modifier
                }
            )
            .then(buildModifier(cell.modifier))
            .padding(8.dp),
        contentAlignment = when (alignment) {
            TextAlign.Start -> Alignment.CenterStart
            TextAlign.Center -> Alignment.Center
            TextAlign.End -> Alignment.CenterEnd
            else -> Alignment.CenterStart
        }
    ) {
        Text(
            text = cell.text,
            style = textStyle
        )
    }
}

@Composable
fun RenderHorizontalPager(node: NodeModel, onAction: (String) -> Unit) {
    val children = node.children ?: emptyList()
    val pagerState = rememberPagerState(pageCount = { children.size })

    HorizontalPager(
        state = pagerState,
        modifier = buildModifier(node.modifier)
    ) { page ->
        RenderNode(children[page], onAction)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewVideoItem() {
    MaterialTheme {
        RenderVideoItem(
            node = NodeModel(
                type = "VideoItem",
                title = "Sample Video Title",
                description = "This is a sample video description that shows how the video item will look in the preview.",
                imageUrl = "https://via.placeholder.com/120",
                backgroundColor = "#FFFFFF",
                elevation = 4f,
                roundedCorners = 12f,
                action = "video_clicked"
            )
        )
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 600)
@Composable
fun PreviewHorizontalPagerWithVideoItems() {
    MaterialTheme {
        RenderHorizontalPager(
            node = NodeModel(
                type = "HorizontalPager",
                children = listOf(
                    NodeModel(
                        type = "VideoItem",
                        title = "Video 1: Introduction",
                        description = "Learn the basics in this introductory video covering all essential concepts.",
                        imageUrl = "https://via.placeholder.com/120",
                        backgroundColor = "#E3F2FD",
                        elevation = 4f,
                        roundedCorners = 12f,
                        action = "video_1_clicked"
                    ),
                    NodeModel(
                        type = "VideoItem",
                        title = "Video 2: Advanced Topics",
                        description = "Dive deeper into advanced topics and best practices for professionals.",
                        imageUrl = "https://via.placeholder.com/120",
                        backgroundColor = "#F3E5F5",
                        elevation = 4f,
                        roundedCorners = 12f,
                        action = "video_2_clicked"
                    ),
                    NodeModel(
                        type = "VideoItem",
                        title = "Video 3: Expert Tips",
                        description = "Expert tips and tricks from industry professionals.",
                        imageUrl = "https://via.placeholder.com/120",
                        backgroundColor = "#E8F5E9",
                        elevation = 4f,
                        roundedCorners = 12f,
                        action = "video_3_clicked"
                    )
                )
            ),
            onAction = { }
        )
    }
}