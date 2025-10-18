package com.cincinnatiai.ssr_simple.model

data class NodeModel(
    val type: String,
    val title: String? = null,
    val description: String? = null,
    val label: String? = null,
    val backgroundColor: String? = null,
    val elevation: Float? = null,
    val imageUrl: String? = null,
    val imageHeight: Float? = null,
    val imageWidth: Float? = null,
    val contentDescription: String? = null,
    val action: String? = null,
    val modifier: ModifierModel? = null,
    val textStyle: TextStyleModel? = null,
    val topBar: NodeModel? = null,
    val floatingActionButton: NodeModel? = null,
    val content: NodeModel? = null,
    val children: List<NodeModel>? = null
)