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
    val children: List<NodeModel>? = null,
    // Button-specific properties
    val buttonVariant: String? = null, // "filled", "outlined", "text" (default: "filled")
    // Table-specific properties
    val columns: List<TableColumnModel>? = null,
    val tableData: List<List<TableCellModel>>? = null,
    val showBorders: Boolean? = null,
    val headerBackgroundColor: String? = null,
    val rowAction: String? = null, // Action when entire row is clicked
    val roundedCorners: Float? = null, // Rounded corner radius in dp for tables
    val useLazyColumn: Boolean? = null // Use LazyColumn for table rows (default: true). Set to false when table is in a scrollable parent
)