package com.cincinnatiai.ssr_library.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

interface ImageLoader {
    @Composable
    fun LoadImage(url: String, modifier: Modifier = Modifier)
}

class CoilImageLoader : ImageLoader {
    // TODO add a placeholder and error image
    @Composable
    override fun LoadImage(url: String, modifier: Modifier) {
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}