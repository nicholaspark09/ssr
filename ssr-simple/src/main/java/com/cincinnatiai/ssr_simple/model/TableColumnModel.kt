package com.cincinnatiai.ssr_simple.model

data class TableColumnModel(
    val header: String,
    val weight: Float? = null, // Column weight for flexible width (e.g., 1f, 2f)
    val width: Int? = null, // Fixed width in dp (overrides weight if set)
    val horizontalAlignment: String? = null, // "start", "center", "end"
    val textStyle: TextStyleModel? = null, // Style for this column's cells
    val headerStyle: TextStyleModel? = null // Style for this column's header
)
