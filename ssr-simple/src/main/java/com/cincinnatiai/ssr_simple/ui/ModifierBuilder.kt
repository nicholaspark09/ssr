package com.cincinnatiai.ssr_simple.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cincinnatiai.ssr_simple.model.ModifierModel
import androidx.core.graphics.toColorInt

fun buildModifier(model: ModifierModel?): Modifier {
    var modifier: Modifier = Modifier

    if (model?.fillMaxSize == true) modifier = modifier.fillMaxSize()
    if (model?.fillMaxWidth == true) modifier = modifier.fillMaxWidth()

    model?.backgroundColor?.let {
        modifier = modifier.background(Color(it.toColorInt()))
    }

    // Apply padding last so it insets the content
    val hasAnyPadding = listOf(
        model?.padding,
        model?.paddingTop,
        model?.paddingBottom,
        model?.paddingStart,
        model?.paddingEnd
    ).any { it != null }

    if (hasAnyPadding) {
        modifier = modifier.padding(
            start = (model?.paddingStart ?: model?.padding ?: 0).dp,
            top = (model?.paddingTop ?: model?.padding ?: 0).dp,
            end = (model?.paddingEnd ?: model?.padding ?: 0).dp,
            bottom = (model?.paddingBottom ?: model?.padding ?: 0).dp
        )
    }

    return modifier
}