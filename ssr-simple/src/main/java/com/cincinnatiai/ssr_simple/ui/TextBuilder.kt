package com.cincinnatiai.ssr_simple.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.cincinnatiai.ssr_simple.model.TextStyleModel
import androidx.core.graphics.toColorInt

@Composable
fun buildTextStyle(model: TextStyleModel?): TextStyle {
    val fontSize = model?.fontSize?.sp ?: MaterialTheme.typography.bodyMedium.fontSize
    val weight = when (model?.fontWeight?.lowercase()) {
        "bold" -> FontWeight.Bold
        "medium" -> FontWeight.Medium
        "light" -> FontWeight.Light
        else -> FontWeight.Normal
    }
    val align = when (model?.textAlign?.lowercase()) {
        "center" -> TextAlign.Center
        "end" -> TextAlign.End
        else -> TextAlign.Start
    }
    val color = model?.color?.let { Color(it.toColorInt()) }
        ?: MaterialTheme.colorScheme.onBackground

    return TextStyle(
        fontSize = fontSize,
        fontWeight = weight,
        textAlign = align,
        color = color
    )
}