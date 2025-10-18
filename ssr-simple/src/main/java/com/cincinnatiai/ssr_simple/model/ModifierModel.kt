package com.cincinnatiai.ssr_simple.model

data class ModifierModel(
    val padding: Int? = null,
    val paddingTop: Int? = null,
    val paddingBottom: Int? = null,
    val paddingStart: Int? = null,
    val paddingEnd: Int? = null,
    val fillMaxSize: Boolean? = null,
    val fillMaxWidth: Boolean? = null,
    val backgroundColor: String? = null,
    val horizontalAlignment: String? = null, // For Column: "start", "center", "end"
    val verticalAlignment: String? = null, // For Row: "top", "center", "bottom"
    val contentAlignment: String? = null // For Box: "topStart", "center", "bottomEnd", etc.
)