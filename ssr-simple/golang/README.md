# SSR-Simple Go Package

A Go package for generating JSON UI definitions for the SSR-Simple Android library. Build type-safe, server-driven UI in Go that renders as native Android Jetpack Compose components.

## Installation

```bash
go get github.com/yourusername/ssr-simple-go
```

Or copy the `ssr_simple.go` file into your project.

## Quick Start

```go
package main

import (
    "fmt"
    "log"

    ssr "path/to/ssr_simple"
)

func main() {
    // Create a simple card
    card := ssr.Card(
        "Welcome",
        "This is a simple card",
        4.0,
        ssr.Modifier().WithFillMaxWidth().WithPadding(16),
    )

    // Convert to JSON
    json, err := card.ToJSONIndent()
    if err != nil {
        log.Fatal(err)
    }

    fmt.Println(json)
}
```

## Core Components

### 1. Scaffold (Full Screen Layout)

```go
screen := ssr.Scaffold(
    "My App",
    ssr.LazyColumn(
        []ssr.NodeModel{
            *ssr.Text("Hello World", ssr.HeadingStyle(), nil),
        },
        ssr.Modifier().WithFillMaxSize().WithPadding(16),
    ),
)
```

### 2. Layout Components

**Column (Vertical)**
```go
column := ssr.Column(
    []ssr.NodeModel{
        *ssr.Text("Item 1", nil, nil),
        *ssr.Text("Item 2", nil, nil),
    },
    ssr.Modifier().WithFillMaxWidth().WithHorizontalAlignment("center"),
)
```

**Row (Horizontal)**
```go
row := ssr.Row(
    []ssr.NodeModel{
        *ssr.Card("42", "Tasks", 2.0, nil),
        *ssr.Card("18", "Projects", 2.0, nil),
    },
    ssr.Modifier().WithFillMaxWidth().WithVerticalAlignment("center"),
)
```

**Box (Stacked)**
```go
box := ssr.Box(
    []ssr.NodeModel{
        *ssr.Text("Background", nil, nil),
        *ssr.Text("Foreground", nil, nil),
    },
    ssr.Modifier().WithContentAlignment("center"),
)
```

**LazyColumn (Scrollable List)**
```go
list := ssr.LazyColumn(
    []ssr.NodeModel{
        *ssr.Card("Item 1", "Description", 2.0, nil),
        *ssr.Card("Item 2", "Description", 2.0, nil),
    },
    ssr.Modifier().WithFillMaxSize(),
)
```

### 3. Display Components

**Text**
```go
text := ssr.Text(
    "Hello World",
    ssr.TextStyle().
        WithFontSize(24).
        WithFontWeight("bold").
        WithColor("#212121"),
    ssr.Modifier().WithPadding(16),
)
```

**Card**
```go
// Simple card
card := ssr.Card(
    "Title",
    "Description",
    4.0, // elevation
    ssr.Modifier().WithFillMaxWidth(),
)

// Card with custom children
card := ssr.CardWithChildren(
    []ssr.NodeModel{
        *ssr.Text("Custom Content", ssr.TitleStyle(), nil),
        *ssr.Button("Action", "navigate:details", nil),
    },
    4.0,
    ssr.Modifier().WithFillMaxWidth(),
)
```

**Image**
```go
image := ssr.Image(
    "https://example.com/image.jpg",
    200, 150, // width, height (0 for natural size)
    "Description",
    ssr.Modifier().WithFillMaxWidth(),
)
```

**Button**
```go
button := ssr.Button(
    "Click Me",
    "navigate:home", // action identifier
    ssr.Modifier().WithFillMaxWidth(),
)
```

### 4. Table Component

**Simple Table**
```go
columns := []ssr.TableColumnModel{
    ssr.NewTableColumn("Name", 2.0, "start"),
    ssr.NewTableColumn("Status", 1.0, "center"),
}

tableData := [][]ssr.TableCellModel{
    {
        ssr.NewTableCell("Alice Johnson"),
        ssr.NewTableCellWithStyle(
            "Active",
            "#4CAF50", // background color
            ssr.WhiteTextStyle(),
        ),
    },
}

table := ssr.Table(
    columns,
    tableData,
    true, // showBorders
    "#6200EE", // headerBackgroundColor
    ssr.Modifier().WithFillMaxWidth(),
)
```

**Table with Click Actions**
```go
// Add row click action
table := ssr.TableWithRowAction(
    columns,
    tableData,
    true,
    "#1976D2",
    "row_clicked", // action when any row is clicked
    ssr.Modifier().WithFillMaxWidth(),
)

// Or add cell-level actions
cell := ssr.NewTableCellWithAction(
    "Buy",
    "#2196F3",
    "buy_product_001", // action when this cell is clicked
    ssr.WhiteTextStyle(),
)
```

**Table with Styled Columns**
```go
column := ssr.NewTableColumnWithStyle(
    "Revenue",
    1.5, // weight
    "end", // alignment
    ssr.TextStyle().WithColor("#FFFFFF").WithFontSize(16), // header style
    ssr.TextStyle().WithFontWeight("bold").WithColor("#2E7D32"), // cell style
)
```

## Modifiers (Layout & Styling)

Modifiers control padding, sizing, alignment, and background:

```go
modifier := ssr.Modifier().
    WithPadding(16).
    WithPaddingTop(8).
    WithFillMaxWidth().
    WithBackgroundColor("#E3F2FD").
    WithHorizontalAlignment("center")
```

**Available Modifier Methods:**
- `WithPadding(int)` - Uniform padding
- `WithPaddingTop/Bottom/Start/End(int)` - Individual sides
- `WithFillMaxSize()` - Fill all available space
- `WithFillMaxWidth()` - Fill width only
- `WithBackgroundColor(string)` - Hex color
- `WithHorizontalAlignment(string)` - For Column: "start", "center", "end"
- `WithVerticalAlignment(string)` - For Row: "top", "center", "bottom"
- `WithContentAlignment(string)` - For Box: "topstart", "center", "bottomend", etc.

## Text Styles

Text styles control font appearance:

```go
style := ssr.TextStyle().
    WithFontSize(18).
    WithFontWeight("bold").
    WithColor("#212121").
    WithTextAlign("center")
```

**Predefined Styles:**
- `ssr.HeadingStyle()` - 24sp bold heading
- `ssr.TitleStyle()` - 18sp bold title
- `ssr.BodyStyle()` - 14sp body text
- `ssr.WhiteTextStyle()` - White bold text (for colored backgrounds)

**Available Text Style Methods:**
- `WithFontSize(float64)` - Size in sp
- `WithFontWeight(string)` - "normal", "medium", "bold", "light"
- `WithTextAlign(string)` - "start", "center", "end"
- `WithColor(string)` - Hex color

## HTTP Server Example

```go
package main

import (
    "encoding/json"
    "net/http"

    ssr "path/to/ssr_simple"
)

func handleDashboard(w http.ResponseWriter, r *http.Request) {
    screen := ssr.Scaffold(
        "Dashboard",
        ssr.LazyColumn(
            []ssr.NodeModel{
                *ssr.Text("Welcome!", ssr.HeadingStyle(), nil),
                *ssr.Card("42", "Tasks", 4.0, ssr.Modifier().WithFillMaxWidth()),
                *ssr.Button("View All", "navigate:tasks", nil),
            },
            ssr.Modifier().WithFillMaxSize().WithPadding(16),
        ),
    )

    w.Header().Set("Content-Type", "application/json")
    json.NewEncoder(w).Encode(screen)
}

func main() {
    http.HandleFunc("/api/dashboard", handleDashboard)
    http.ListenAndServe(":8080", nil)
}
```

## Complete Example: Product Catalog

```go
func createProductCatalog(products []Product) *ssr.NodeModel {
    var cards []ssr.NodeModel

    for _, product := range products {
        card := ssr.CardWithChildren(
            []ssr.NodeModel{
                *ssr.Image(
                    product.ImageURL,
                    0, 0,
                    product.Name,
                    ssr.Modifier().WithFillMaxWidth(),
                ),
                *ssr.Column(
                    []ssr.NodeModel{
                        *ssr.Text(product.Name, ssr.TitleStyle(), nil),
                        *ssr.Text(
                            product.Description,
                            ssr.BodyStyle(),
                            ssr.Modifier().WithPaddingTop(4),
                        ),
                        *ssr.Row(
                            []ssr.NodeModel{
                                *ssr.Text(
                                    product.Price,
                                    ssr.TextStyle().
                                        WithFontSize(18).
                                        WithFontWeight("bold").
                                        WithColor("#2E7D32"),
                                    nil,
                                ),
                                *ssr.Box(
                                    []ssr.NodeModel{
                                        *ssr.Button(
                                            "Add to Cart",
                                            "add:"+product.ID,
                                            nil,
                                        ),
                                    },
                                    ssr.Modifier().
                                        WithFillMaxWidth().
                                        WithContentAlignment("centerend"),
                                ),
                            },
                            ssr.Modifier().
                                WithFillMaxWidth().
                                WithVerticalAlignment("center").
                                WithPaddingTop(8),
                        ),
                    },
                    ssr.Modifier().WithPadding(16),
                ),
            },
            4.0,
            ssr.Modifier().WithFillMaxWidth().WithPaddingBottom(12),
        )
        cards = append(cards, *card)
    }

    return ssr.Scaffold(
        "Products",
        ssr.LazyColumn(
            cards,
            ssr.Modifier().WithFillMaxSize().WithPadding(16),
        ),
    )
}
```

## Color Guidelines

Always use 6-digit hex format with `#` prefix:

**Material Design Colors:**
- Primary Blue: `#1976D2`, `#2196F3`
- Success Green: `#4CAF50`, `#2E7D32`
- Warning Orange: `#FF9800`, `#F57C00`
- Error Red: `#F44336`, `#D32F2F`
- Purple: `#6200EE`, `#9C27B0`
- Gray: `#9E9E9E`, `#757575`, `#212121`
- Light Backgrounds: `#E3F2FD`, `#E8F5E9`, `#FFF9C4`

## Helper Functions

**Pointer Helpers** (required for omitempty JSON fields):
```go
ssr.Int(16)       // *int
ssr.Float64(4.0)  // *float64
ssr.String("text") // *string
ssr.Bool(true)    // *bool
```

**JSON Conversion:**
```go
json, _ := node.ToJSON()        // Compact JSON
json, _ := node.ToJSONIndent()  // Pretty-printed JSON
```

## Examples Directory

Check the `examples/` directory for:
- `basic_example.go` - Simple UI components
- `table_example.go` - Table creation examples
- `http_server.go` - Full HTTP server with multiple endpoints

## License

Same as SSR-Simple Android library
