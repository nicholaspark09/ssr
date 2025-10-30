# SSR-Simple

A lightweight Android Compose-based server-side rendering library that converts JSON into native Compose UI components.

## Features

### Supported Components

- **Scaffold** - App layout with top bar, FAB, and content area
- **TopAppBar** - Application top bar with title
- **Column** - Vertical layout container with alignment support
- **Row** - Horizontal layout container with alignment support
- **Box** - Stacked layout container with content alignment
- **LazyColumn** - Scrollable vertical list for performance
- **Card** - Material Design card with customizable elevation
- **Text** - Text display with custom styling
- **Button** - Interactive button with action handling
- **Image** - Async image loading with size control
- **Table** - Data table with headers, flexible columns, cell styling, and click actions

## New Features

### Card Elevation

Cards now support custom elevation through the `elevation` property:

```json
{
  "type": "Card",
  "title": "Elevated Card",
  "description": "This card has custom elevation",
  "elevation": 12
}
```

### Layout Alignment

#### Column Horizontal Alignment

```json
{
  "type": "Column",
  "modifier": {
    "horizontalAlignment": "center"
  }
}
```

Options: `"start"`, `"center"`, `"end"`

#### Row Vertical Alignment

```json
{
  "type": "Row",
  "modifier": {
    "verticalAlignment": "center"
  }
}
```

Options: `"top"`, `"center"`, `"bottom"`

#### Box Content Alignment

```json
{
  "type": "Box",
  "modifier": {
    "contentAlignment": "center"
  }
}
```

Options: `"topstart"`, `"topcenter"`, `"topend"`, `"centerstart"`, `"center"`, `"centerend"`, `"bottomstart"`, `"bottomcenter"`, `"bottomend"`

### Image Loading

Load images from URLs with automatic sizing:

```json
{
  "type": "Image",
  "imageUrl": "https://example.com/image.jpg",
  "imageWidth": 100,
  "imageHeight": 100,
  "contentDescription": "Description for accessibility"
}
```

- Omit both width/height for natural size
- Set only width or height to constrain one dimension
- Set both for fixed size
- Uses Coil for async loading and caching

### Table Component

Display structured data with customizable headers, columns, and cell styling:

```json
{
  "type": "Table",
  "showBorders": true,
  "headerBackgroundColor": "#6200EE",
  "rowAction": "row_clicked",
  "columns": [
    {
      "header": "Name",
      "weight": 2.0,
      "horizontalAlignment": "start",
      "headerStyle": {
        "color": "#FFFFFF",
        "fontSize": 16
      }
    },
    {
      "header": "Status",
      "weight": 1.0,
      "horizontalAlignment": "center"
    }
  ],
  "tableData": [
    [
      {
        "text": "Alice Johnson"
      },
      {
        "text": "Active",
        "backgroundColor": "#4CAF50",
        "textStyle": {
          "color": "#FFFFFF",
          "fontWeight": "bold"
        },
        "action": "view_alice"
      }
    ]
  ]
}
```

**Features:**
- **Flexible columns** - Use `weight` for proportional widths or `width` (dp) for fixed sizes
- **Header styling** - Custom background colors and text styles for headers
- **Cell styling** - Override styles per cell with `backgroundColor` and `textStyle`
- **Click actions** - `rowAction` for entire row clicks, or `action` per cell
- **Borders** - Optional borders with `showBorders: true`
- **Alignment** - Per-column alignment: `"start"`, `"center"`, `"end"`
- **Performance** - Uses LazyColumn for efficient rendering of large datasets

## JSON Schema

### NodeModel

```json
{
  "type": "String",              // Component type (required)
  "title": "String",             // Primary text/heading
  "description": "String",       // Secondary text content
  "label": "String",             // Button/input labels
  "backgroundColor": "#RRGGBB",  // Background color (hex)
  "elevation": 4.0,              // Card elevation in dp
  "imageUrl": "String",          // Image URL for Image component
  "imageWidth": 100.0,           // Image width in dp
  "imageHeight": 100.0,          // Image height in dp
  "contentDescription": "String", // Accessibility description for images
  "action": "String",            // Action identifier
  "modifier": { },               // ModifierModel (see below)
  "textStyle": { },              // TextStyleModel (see below)
  "topBar": { },                 // NodeModel for Scaffold
  "floatingActionButton": { },   // NodeModel for Scaffold
  "content": { },                // NodeModel for Scaffold
  "children": [],                // Array of NodeModels
  // Table-specific properties
  "columns": [],                 // Array of TableColumnModel (for Table)
  "tableData": [],               // Array of arrays of TableCellModel (for Table)
  "showBorders": true,           // Show table borders (for Table)
  "headerBackgroundColor": "#RRGGBB", // Header background color (for Table)
  "rowAction": "String"          // Action when row is clicked (for Table)
}
```

### TableColumnModel

```json
{
  "header": "String",            // Column header text (required)
  "weight": 1.0,                 // Column width weight (flexible)
  "width": 100,                  // Fixed column width in dp (overrides weight)
  "horizontalAlignment": "start", // "start", "center", "end"
  "textStyle": { },              // TextStyleModel for column cells
  "headerStyle": { }             // TextStyleModel for header
}
```

### TableCellModel

```json
{
  "text": "String",              // Cell text content (required)
  "textStyle": { },              // TextStyleModel (overrides column style)
  "backgroundColor": "#RRGGBB",  // Cell background color (hex)
  "action": "String",            // Click action for this cell
  "modifier": { }                // ModifierModel for additional styling
}
```

### ModifierModel

```json
{
  "padding": 16,                      // Uniform padding (dp)
  "paddingTop": 8,                    // Top padding (dp)
  "paddingBottom": 8,                 // Bottom padding (dp)
  "paddingStart": 8,                  // Start padding (dp)
  "paddingEnd": 8,                    // End padding (dp)
  "fillMaxSize": true,                // Fill entire space
  "fillMaxWidth": true,               // Fill width only
  "backgroundColor": "#RRGGBB",       // Background color (hex)
  "horizontalAlignment": "center",    // For Column
  "verticalAlignment": "center",      // For Row
  "contentAlignment": "center"        // For Box
}
```

### TextStyleModel

```json
{
  "fontSize": 16.0,              // Font size in sp
  "fontWeight": "bold",          // "normal", "medium", "bold", "light"
  "textAlign": "center",         // "start", "center", "end"
  "color": "#RRGGBB"             // Text color (hex)
}
```

## Usage

### Basic Usage

```kotlin
import com.cincinnatiai.ssr_simple.SSRSimpleLibrary

SSRSimpleLibrary.ShowScreen(
    jsonString = """{ "type": "Card", "title": "Hello World" }""",
    onNavigate = { route -> /* Handle navigation */ }
)
```

### Preview Screen

A built-in preview screen is available for testing and developing JSON layouts:

```kotlin
import com.cincinnatiai.ssr_simple.ui.PreviewScreen

PreviewScreen(
    initialJson = """{ "type": "Text", "title": "Preview" }"""
)
```

The preview screen features:
- Live JSON editor with syntax highlighting
- Real-time preview of rendered UI
- Error display for invalid JSON

## Examples

Sample JSON files demonstrating various features are available in:
`src/main/assets/examples/`

### Basic Examples
- `basic_card.json` - Card elevation examples
- `column_alignment.json` - Column horizontal alignment
- `row_alignment.json` - Row vertical alignment
- `box_alignment.json` - Box content alignment
- `complete_example.json` - Comprehensive feature showcase
- `simple_table.json` - Basic table with employee directory and status indicators

### Complex Examples
- `complex_dashboard.json` - Full dashboard with stats, activity feed, team updates, and multiple interactive components
- `product_catalog.json` - E-commerce product listing with categories, featured products, pricing, and cart actions
- `image_gallery.json` - Image loading examples with various sizes, grid layouts, and profile cards
- `advanced_table.json` - Sales dashboard with styled cells, click actions, and color-coded revenue data

## LLM-Assisted UI Generation

Two context files are provided to help LLMs generate SSR-Simple JSON:

- **LLM_CONTEXT.md** - Comprehensive guide with detailed explanations, all component properties, patterns, and color suggestions. Use this for in-depth understanding.
- **LLM_PROMPT.txt** - Concise prompt for quick reference. Copy this into your LLM conversation to enable UI generation from natural language descriptions.

### Example Usage
```
[Copy contents of LLM_PROMPT.txt into your LLM chat]

User: "Create a profile screen with a header card showing user info,
       a statistics row with followers/following counts, and a button to edit profile"

LLM: [Generates valid SSR-Simple JSON]
```

## Architecture

```
Entry Point (SSRSimpleLibrary.ShowScreen)
    ↓
JSON Parsing (DeserializerProvider)
    ↓
Data Models (NodeModel, ModifierModel, TextStyleModel)
    ↓
Rendering Pipeline (RenderNode dispatcher)
    ↓
Compose UI Output
```

## Dependencies

- Jetpack Compose (Material3)
- Gson for JSON parsing
- Android SDK 24+

## License

Copyright 2024
