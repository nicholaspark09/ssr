package com.cincinnatiai.ssr_simple.model

data class TableCellModel(
    val text: String,
    val textStyle: TextStyleModel? = null, // Override column/table text style
    val backgroundColor: String? = null, // Cell background color
    val action: String? = null, // Click action for this cell
    val modifier: ModifierModel? = null, // Additional modifiers for the cell
    val showBorder: Boolean? = null // Override table border setting for this cell
)
