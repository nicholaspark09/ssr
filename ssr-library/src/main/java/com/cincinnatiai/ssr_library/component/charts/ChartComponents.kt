package com.cincinnatiai.ssr_library.component.charts

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.*
import androidx.core.graphics.toColorInt

data class ChartDataPoint(
    val label: String,
    val value: Double,
    val color: String? = null,
    val metadata: Map<String, Any>? = null,
)

data class ChartSeries(
    val name: String,
    val data: List<ChartDataPoint>,
    val color: String? = null,
)

data class ChartConfig(
    val title: String? = null,
    val subtitle: String? = null,
    val showLegend: Boolean = true,
    val showGrid: Boolean = true,
    val showLabels: Boolean = true,
    val showValues: Boolean = false,
    val animated: Boolean = true,
    val colors: List<String>? = null,
    val height: Int? = null,
    val width: Int? = null,
)

@Composable
fun BarChart(
    data: List<ChartDataPoint>,
    config: ChartConfig = ChartConfig(),
    modifier: Modifier = Modifier,
) {
    val defaultColors = listOf(
        "#3B82F6", "#EF4444", "#10B981", "#F59E0B",
        "#8B5CF6", "#EC4899", "#06B6D4", "#84CC16"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            config.title?.let { title ->
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                config.subtitle?.let { subtitle ->
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((config.height ?: 300).dp)
            ) {
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    drawBarChart(data, config, defaultColors)
                }
            }

            // Legend
            if (config.showLegend && data.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                ChartLegend(data, defaultColors)
            }
        }
    }
}

@Composable
fun LineChart(
    series: List<ChartSeries>,
    config: ChartConfig = ChartConfig(),
    modifier: Modifier = Modifier,
    props: Map<String, Any>,
) {
    val elevation = (props["elevation"] as? Number)?.toFloat() ?: 2f
    val backgroundColor = props["backgroundColor"] as? String
    val bgColor = backgroundColor?.let { parseColor(it) } ?: Color.White
    val defaultColors = listOf(
        "#3B82F6", "#EF4444", "#10B981", "#F59E0B",
        "#8B5CF6", "#EC4899", "#06B6D4", "#84CC16"
    )
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            config.title?.let { title ->
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                config.subtitle?.let { subtitle ->
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((config.height ?: 300).dp)
            ) {
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    drawLineChart(series, config, defaultColors)
                }
            }

            // Legend
            if (config.showLegend && series.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                SeriesLegend(series, defaultColors)
            }
        }
    }
}

@Composable
fun PieChart(
    data: List<ChartDataPoint>,
    config: ChartConfig = ChartConfig(),
    modifier: Modifier = Modifier,
) {
    val defaultColors = listOf(
        "#3B82F6", "#EF4444", "#10B981", "#F59E0B",
        "#8B5CF6", "#EC4899", "#06B6D4", "#84CC16"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            config.title?.let { title ->
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                config.subtitle?.let { subtitle ->
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Row {
                // Chart
                Box(
                    modifier = Modifier
                        .size((config.height ?: 250).dp)
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(
                        modifier = Modifier.size(200.dp)
                    ) {
                        drawPieChart(data, config, defaultColors)
                    }
                }

                // Legend
                if (config.showLegend && data.isNotEmpty()) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        ChartLegend(data, defaultColors)
                    }
                }
            }
        }
    }
}

@Composable
fun BubbleChart(
    data: List<ChartDataPoint>,
    config: ChartConfig = ChartConfig(),
    modifier: Modifier = Modifier,
) {
    val defaultColors = listOf(
        "#3B82F6", "#EF4444", "#10B981", "#F59E0B",
        "#8B5CF6", "#EC4899", "#06B6D4", "#84CC16"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            config.title?.let { title ->
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                config.subtitle?.let { subtitle ->
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((config.height ?: 300).dp)
            ) {
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    drawBubbleChart(data, config, defaultColors)
                }
            }

            // Legend
            if (config.showLegend && data.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                ChartLegend(data, defaultColors)
            }
        }
    }
}

@Composable
fun RadarChart(
    data: List<ChartDataPoint>,
    config: ChartConfig = ChartConfig(),
    modifier: Modifier = Modifier,
) {
    val defaultColors = listOf(
        "#3B82F6", "#EF4444", "#10B981", "#F59E0B",
        "#8B5CF6", "#EC4899", "#06B6D4", "#84CC16"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            config.title?.let { title ->
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                config.subtitle?.let { subtitle ->
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((config.height ?: 300).dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier.size(250.dp)
                ) {
                    drawRadarChart(data, config, defaultColors)
                }
            }

            // Legend
            if (config.showLegend && data.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                ChartLegend(data, defaultColors)
            }
        }
    }
}

private fun DrawScope.drawBarChart(
    data: List<ChartDataPoint>,
    config: ChartConfig,
    defaultColors: List<String>,
) {
    if (data.isEmpty()) return

    val maxValue = data.maxOfOrNull { it.value } ?: 1.0
    val chartWidth = size.width - 80.dp.toPx()
    val chartHeight = size.height - 80.dp.toPx()
    val barWidth = chartWidth / data.size * 0.8f
    val spacing = chartWidth / data.size * 0.2f

    // Draw grid
    if (config.showGrid) {
        for (i in 0..5) {
            val y = 40.dp.toPx() + (chartHeight / 5) * i
            drawLine(
                color = Color.Gray.copy(alpha = 0.3f),
                start = Offset(40.dp.toPx(), y),
                end = Offset(size.width - 40.dp.toPx(), y),
                strokeWidth = 1.dp.toPx()
            )
        }
    }

    // Draw bars
    data.forEachIndexed { index, point ->
        val barHeight = (point.value / maxValue * chartHeight).toFloat()
        val x = 40.dp.toPx() + (barWidth + spacing) * index + spacing / 2
        val y = size.height - 40.dp.toPx() - barHeight

        var color = parseHexColor(
            point.color ?: config.colors?.getOrNull(index) ?: defaultColors[index % defaultColors.size]
        )

        drawRoundRect(
            color = color,
            topLeft = Offset(x, y),
            size = Size(barWidth, barHeight),
            cornerRadius = CornerRadius(4.dp.toPx())
        )

        // Draw value labels
        if (config.showValues) {
            val textPaint = createTextPaint(
                color = Color.Black,
                textSize = 12.sp.toPx()
            )
            drawContext.canvas.nativeCanvas.drawText(
                point.value.toInt().toString(),
                x + barWidth / 2,
                y - 8.dp.toPx(),
                textPaint
            )
        }
    }
}

private fun DrawScope.drawLineChart(
    series: List<ChartSeries>,
    config: ChartConfig,
    defaultColors: List<String>,
) {
    if (series.isEmpty() || series.all { it.data.isEmpty() }) return

    val allPoints = series.flatMap { it.data }
    val maxValue = allPoints.maxOfOrNull { it.value } ?: 1.0
    val minValue = allPoints.minOfOrNull { it.value } ?: 0.0
    val chartWidth = size.width - 80.dp.toPx()
    val chartHeight = size.height - 80.dp.toPx()

    if (config.showGrid) {
        for (i in 0..5) {
            val y = 40.dp.toPx() + (chartHeight / 5) * i
            drawLine(
                color = Color.Gray.copy(alpha = 0.3f),
                start = Offset(40.dp.toPx(), y),
                end = Offset(size.width - 40.dp.toPx(), y),
                strokeWidth = 1.dp.toPx()
            )
        }
    }

    // Draw lines for each series
    series.forEachIndexed { seriesIndex, chartSeries ->
        if (chartSeries.data.isEmpty()) return@forEachIndexed

        val color = parseHexColor(
            chartSeries.color ?: config.colors?.getOrNull(seriesIndex) ?: defaultColors[seriesIndex % defaultColors.size]
        )

        val points = chartSeries.data.mapIndexed { index, point ->
            val x = 40.dp.toPx() + (chartWidth / (chartSeries.data.size - 1)) * index
            val y = size.height - 40.dp.toPx() - ((point.value - minValue) / (maxValue - minValue) * chartHeight).toFloat()
            Offset(x, y)
        }

        // Draw line
        for (i in 0 until points.size - 1) {
            drawLine(
                color = color,
                start = points[i],
                end = points[i + 1],
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        }

        // Draw points
        points.forEach { point ->
            drawCircle(
                color = color,
                radius = 4.dp.toPx(),
                center = point
            )
            drawCircle(
                color = Color.White,
                radius = 2.dp.toPx(),
                center = point
            )
        }
    }
}

private fun DrawScope.drawPieChart(
    data: List<ChartDataPoint>,
    config: ChartConfig,
    defaultColors: List<String>,
) {
    if (data.isEmpty()) return

    val total = data.sumOf { it.value }
    val center = Offset(size.width / 2, size.height / 2)
    val radius = minOf(size.width, size.height) / 2 * 0.8f

    var startAngle = -90f

    data.forEachIndexed { index, point ->
        val sweepAngle = (point.value / total * 360).toFloat()
        var color = parseHexColor(
            point.color ?: config.colors?.getOrNull(index) ?: defaultColors[index % defaultColors.size]
        )

        drawArc(
            color = color,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = true,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2)
        )

        // Draw percentage labels
        if (config.showValues) {
            val midAngle = startAngle + sweepAngle / 2
            val labelRadius = radius * 0.7f
            val labelX = center.x + cos(Math.toRadians(midAngle.toDouble())).toFloat() * labelRadius
            val labelY = center.y + sin(Math.toRadians(midAngle.toDouble())).toFloat() * labelRadius

            val percentage = (point.value / total * 100).toInt()
            if (percentage > 5) { // Only show label if slice is large enough
                val percentagePaint = createTextPaint(
                    color = Color.White,
                    textSize = 12.sp.toPx()
                ).apply {
                    isFakeBoldText = true
                }
                drawContext.canvas.nativeCanvas.drawText(
                    "$percentage%",
                    labelX,
                    labelY,
                    percentagePaint
                )
            }
        }

        startAngle += sweepAngle
    }
}

private fun createTextPaint(color: Color, textSize: Float): Paint {
    return Paint().apply {
        this.color = color.toArgb()
        this.textSize = textSize
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
        isFakeBoldText = true
    }
}

private fun DrawScope.drawBubbleChart(
    data: List<ChartDataPoint>,
    config: ChartConfig,
    defaultColors: List<String>,
) {
    if (data.isEmpty()) return

    val maxValue = data.maxOfOrNull { it.value } ?: 1.0
    val chartWidth = size.width - 80.dp.toPx()
    val chartHeight = size.height - 80.dp.toPx()

    // Draw grid
    if (config.showGrid) {
        for (i in 0..5) {
            val y = 40.dp.toPx() + (chartHeight / 5) * i
            drawLine(
                color = Color.Gray.copy(alpha = 0.3f),
                start = Offset(40.dp.toPx(), y),
                end = Offset(size.width - 40.dp.toPx(), y),
                strokeWidth = 1.dp.toPx()
            )
        }
    }

    // Draw bubbles
    data.forEachIndexed { index, point ->
        val bubbleSize = (point.value / maxValue * 60.dp.toPx()).toFloat()
        val x = 40.dp.toPx() + (chartWidth / data.size) * (index + 0.5f)
        val y = size.height / 2 + (index % 2 - 0.5f) * 100.dp.toPx()

        var color = parseHexColor(
            point.color ?: config.colors?.getOrNull(index) ?: defaultColors[index % defaultColors.size]
        )

        drawCircle(
            color = color.copy(alpha = 0.7f),
            radius = bubbleSize / 2,
            center = Offset(x, y)
        )

        drawCircle(
            color = color,
            radius = bubbleSize / 2,
            center = Offset(x, y),
            style = Stroke(width = 2.dp.toPx())
        )

        // Draw labels
        if (config.showLabels) {
            val labelPaint = createTextPaint(
                color = Color.White,
                textSize = 10.sp.toPx()
            ).apply {
                isFakeBoldText = true
            }
            drawContext.canvas.nativeCanvas.drawText(
                point.label,
                x,
                y,
                labelPaint
            )
        }
    }
}

private fun DrawScope.drawRadarChart(
    data: List<ChartDataPoint>,
    config: ChartConfig,
    defaultColors: List<String>,
) {
    if (data.isEmpty()) return

    val center = Offset(size.width / 2, size.height / 2)
    val radius = minOf(size.width, size.height) / 2 * 0.8f
    val maxValue = data.maxOfOrNull { it.value } ?: 1.0
    val angleStep = 360f / data.size

    // Draw grid circles
    if (config.showGrid) {
        for (i in 1..5) {
            val gridRadius = radius * i / 5
            drawCircle(
                color = Color.Gray.copy(alpha = 0.3f),
                radius = gridRadius,
                center = center,
                style = Stroke(width = 1.dp.toPx())
            )
        }

        // Draw grid lines
        data.forEachIndexed { index, _ ->
            val angle = -90f + angleStep * index
            val endX = center.x + cos(Math.toRadians(angle.toDouble())).toFloat() * radius
            val endY = center.y + sin(Math.toRadians(angle.toDouble())).toFloat() * radius

            drawLine(
                color = Color.Gray.copy(alpha = 0.3f),
                start = center,
                end = Offset(endX, endY),
                strokeWidth = 1.dp.toPx()
            )
        }
    }

    // Draw data polygon
    val points = data.mapIndexed { index, point ->
        val angle = -90f + angleStep * index
        val pointRadius = (point.value / maxValue * radius).toFloat()
        val x = center.x + cos(Math.toRadians(angle.toDouble())).toFloat() * pointRadius
        val y = center.y + sin(Math.toRadians(angle.toDouble())).toFloat() * pointRadius
        Offset(x, y)
    }

    // Draw filled polygon
    val path = Path().apply {
        if (points.isNotEmpty()) {
            moveTo(points[0].x, points[0].y)
            for (i in 1 until points.size) {
                lineTo(points[i].x, points[i].y)
            }
            close()
        }
    }

    drawPath(
        path = path,
        color = parseHexColor(defaultColors[0]).copy(alpha = 0.3f)
    )

    drawPath(
        path = path,
        color = parseHexColor(defaultColors[0]),
        style = Stroke(width = 2.dp.toPx())
    )

    // Draw data points
    points.forEach { point ->
        drawCircle(
            color = parseHexColor(defaultColors[0]),
            radius = 4.dp.toPx(),
            center = point
        )
    }

    // Draw labels
    if (config.showLabels) {
        data.forEachIndexed { index, point ->
            val angle = -90f + angleStep * index
            val labelRadius = radius + 20.dp.toPx()
            val labelX = center.x + cos(Math.toRadians(angle.toDouble())).toFloat() * labelRadius
            val labelY = center.y + sin(Math.toRadians(angle.toDouble())).toFloat() * labelRadius

            val labelPaint = createTextPaint(
                color = Color.Black,
                textSize = 12.sp.toPx()
            )
            drawContext.canvas.nativeCanvas.drawText(
                point.label,
                labelX,
                labelY,
                labelPaint
            )
        }
    }
}

@Composable
private fun ChartLegend(
    data: List<ChartDataPoint>,
    defaultColors: List<String>,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(data.size) { index ->
            val point = data[index]
            val color = parseHexColor(
                point.color ?: defaultColors[index % defaultColors.size]
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(color)
                )
                Text(
                    text = point.label,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun SeriesLegend(
    series: List<ChartSeries>,
    defaultColors: List<String>,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(series.size) { index ->
            val chartSeries = series[index]
            val color = parseHexColor(
                chartSeries.color ?: defaultColors[index % defaultColors.size]
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(color)
                )
                Text(
                    text = chartSeries.name,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

private fun parseHexColor(hex: String): Color {
    return try {
        val cleanHex = hex.removePrefix("#")
        val colorLong = cleanHex.toLong(16)
        when (cleanHex.length) {
            6 -> Color(0xFF000000 or colorLong)
            8 -> Color(colorLong)
            else -> Color.Blue
        }
    } catch (e: Exception) {
        Color.Blue
    }
}

fun parseColor(colorString: String): Color {
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

fun parseColorV2(colorString: String): Color {
    return try {
        val cleanColor = colorString.removePrefix("#")
        when (cleanColor.length) {
            6 -> {
                // Standard hex format #RRGGBB
                val colorLong = cleanColor.toLong(16)
                Color(0xFF000000 or colorLong)
            }
            8 -> {
                // Hex with alpha #RRGGBBAA
                val colorLong = cleanColor.toLong(16)
                Color(colorLong)
            }
            3 -> {
                // Short hex format #RGB -> #RRGGBB
                val r = cleanColor[0]
                val g = cleanColor[1]
                val b = cleanColor[2]
                val expandedColor = "$r$r$g$g$b$b"
                Color(0xFF000000 or expandedColor.toLong(16))
            }
            else -> Color.Black
        }
    } catch (e: NumberFormatException) {
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
