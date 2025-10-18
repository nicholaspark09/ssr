# SSR-Simple Library Context for LLMs

This document provides comprehensive context about the SSR-Simple library to help LLMs generate valid JSON for rendering Android Compose UI components.

## What is SSR-Simple?

SSR-Simple is an Android library that converts JSON into native Jetpack Compose UI components. It allows you to define entire screens and UI layouts using JSON structures that are then rendered as Material Design 3 components.

## Core Concept

Every UI element is represented by a `NodeModel` in JSON with these key properties:
- `type`: The component type (required)
- `modifier`: Layout and styling properties
- `textStyle`: Text formatting properties
- `children`: Array of child components (for containers)
- Component-specific properties (title, description, etc.)

## Available Components

### 1. Scaffold
Full-screen layout with top bar and content area.

**Properties:**
- `topBar`: NodeModel for the top app bar
- `floatingActionButton`: NodeModel for FAB (optional)
- `content`: NodeModel for main content area

**Example:**
```json
{
  "type": "Scaffold",
  "topBar": { "type": "TopAppBar", "title": "My App" },
  "content": { "type": "Column", "children": [...] }
}
```

### 2. TopAppBar
Material Design top app bar.

**Properties:**
- `title`: String - The title text to display

**Example:**
```json
{
  "type": "TopAppBar",
  "title": "Dashboard"
}
```

### 3. Column
Vertical layout container that stacks children vertically.

**Properties:**
- `children`: Array of NodeModels
- `modifier.horizontalAlignment`: "start" | "center" | "end"

**Example:**
```json
{
  "type": "Column",
  "modifier": {
    "fillMaxWidth": true,
    "padding": 16,
    "horizontalAlignment": "center"
  },
  "children": [...]
}
```

### 4. Row
Horizontal layout container that places children side by side.

**Properties:**
- `children`: Array of NodeModels
- `modifier.verticalAlignment`: "top" | "center" | "bottom"

**Example:**
```json
{
  "type": "Row",
  "modifier": {
    "fillMaxWidth": true,
    "verticalAlignment": "center"
  },
  "children": [...]
}
```

### 5. Box
Stacked layout container where children are placed on top of each other.

**Properties:**
- `children`: Array of NodeModels
- `modifier.contentAlignment`: "topstart" | "topcenter" | "topend" | "centerstart" | "center" | "centerend" | "bottomstart" | "bottomcenter" | "bottomend"

**Example:**
```json
{
  "type": "Box",
  "modifier": {
    "fillMaxWidth": true,
    "padding": 24,
    "contentAlignment": "center"
  },
  "children": [...]
}
```

### 6. LazyColumn
Performance-optimized scrollable vertical list for large datasets.

**Properties:**
- `children`: Array of NodeModels
- `modifier`: Standard modifier properties

**Example:**
```json
{
  "type": "LazyColumn",
  "modifier": {
    "fillMaxSize": true,
    "padding": 16
  },
  "children": [...]
}
```

### 7. Card
Material Design card with elevation and background color.

**Properties:**
- `title`: String - Primary heading (ignored if children provided)
- `description`: String - Secondary text (ignored if children provided)
- `elevation`: Number - Elevation in dp (default: 4)
- `backgroundColor`: String - Hex color (e.g., "#E3F2FD")
- `children`: Array of NodeModels (optional, for custom card content)

**Behavior:**
- If `children` is provided, renders the children without default padding
- If no `children`, renders `title` and `description` with 16dp padding
- Use `children` for complex card layouts with custom content

**Simple Card Example:**
```json
{
  "type": "Card",
  "title": "Card Title",
  "description": "Card description text",
  "elevation": 8,
  "backgroundColor": "#E3F2FD",
  "modifier": {
    "fillMaxWidth": true,
    "paddingBottom": 12
  }
}
```

**Card with Custom Children Example:**
```json
{
  "type": "Card",
  "elevation": 6,
  "backgroundColor": "#FFFFFF",
  "modifier": {
    "fillMaxWidth": true
  },
  "children": [
    {
      "type": "Column",
      "modifier": {
        "fillMaxWidth": true,
        "padding": 16
      },
      "children": [
        {
          "type": "Text",
          "title": "Custom Content",
          "textStyle": {
            "fontSize": 18,
            "fontWeight": "bold"
          }
        },
        {
          "type": "Button",
          "label": "Action",
          "action": "navigate:details"
        }
      ]
    }
  ]
}
```

### 8. Text
Text display with custom styling.

**Properties:**
- `title`: String - Main text content
- `description`: String - Alternative text content (renders with bodyMedium style)
- `textStyle`: TextStyleModel for custom styling

**Example:**
```json
{
  "type": "Text",
  "title": "Hello World",
  "textStyle": {
    "fontSize": 20,
    "fontWeight": "bold",
    "color": "#212121"
  }
}
```

### 9. Button
Interactive button with action handling.

**Properties:**
- `label`: String - Button text
- `action`: String - Action identifier (e.g., "navigate:details")

**Example:**
```json
{
  "type": "Button",
  "label": "Click Me",
  "action": "navigate:home"
}
```

### 10. Image
Async image loading from URLs with Coil.

**Properties:**
- `imageUrl`: String - URL of the image to load (required)
- `imageWidth`: Number - Width in dp (optional)
- `imageHeight`: Number - Height in dp (optional)
- `contentDescription`: String - Accessibility description
- `modifier`: Standard modifier properties

**Size Behavior:**
- No width/height: Image displays at natural size
- Width only: Constrains width, height scales proportionally
- Height only: Constrains height, width scales proportionally
- Both: Fixed size (may crop image)

**Simple Image Example:**
```json
{
  "type": "Image",
  "imageUrl": "https://picsum.photos/400/300",
  "contentDescription": "Beautiful landscape",
  "modifier": {
    "fillMaxWidth": true
  }
}
```

**Fixed Size Image Example:**
```json
{
  "type": "Image",
  "imageUrl": "https://example.com/profile.jpg",
  "imageWidth": 60,
  "imageHeight": 60,
  "contentDescription": "Profile picture"
}
```

**Image in Card Example:**
```json
{
  "type": "Card",
  "elevation": 4,
  "modifier": {
    "fillMaxWidth": true
  },
  "children": [
    {
      "type": "Column",
      "modifier": {
        "fillMaxWidth": true
      },
      "children": [
        {
          "type": "Image",
          "imageUrl": "https://picsum.photos/400/250",
          "contentDescription": "Product photo",
          "modifier": {
            "fillMaxWidth": true
          }
        },
        {
          "type": "Column",
          "modifier": {
            "padding": 16
          },
          "children": [
            {
              "type": "Text",
              "title": "Product Name",
              "textStyle": {
                "fontSize": 18,
                "fontWeight": "bold"
              }
            }
          ]
        }
      ]
    }
  ]
}
```

## Modifier Properties

The `modifier` object controls layout and styling:

```json
{
  "modifier": {
    "padding": 16,              // Uniform padding in dp
    "paddingTop": 8,            // Individual padding sides
    "paddingBottom": 8,
    "paddingStart": 8,
    "paddingEnd": 8,
    "fillMaxSize": true,        // Fill entire available space
    "fillMaxWidth": true,       // Fill width only
    "backgroundColor": "#RRGGBB", // Hex color
    "horizontalAlignment": "center",  // For Column: "start"|"center"|"end"
    "verticalAlignment": "center",    // For Row: "top"|"center"|"bottom"
    "contentAlignment": "center"      // For Box: see alignment options above
  }
}
```

## TextStyle Properties

The `textStyle` object controls text appearance:

```json
{
  "textStyle": {
    "fontSize": 16.0,           // Size in sp
    "fontWeight": "bold",       // "normal"|"medium"|"bold"|"light"
    "textAlign": "center",      // "start"|"center"|"end"
    "color": "#RRGGBB"          // Hex color
  }
}
```

## Color Format

Always use 6-digit hex format with # prefix:
- Correct: `"#E3F2FD"`, `"#1976D2"`, `"#FFFFFF"`
- Incorrect: `"E3F2FD"`, `"#E3F"`, `"rgb(227, 242, 253)"`

## Common Patterns

### Pattern 1: Card with Button
```json
{
  "type": "Card",
  "elevation": 4,
  "modifier": { "fillMaxWidth": true },
  "children": [
    {
      "type": "Column",
      "modifier": { "fillMaxWidth": true },
      "children": [
        {
          "type": "Text",
          "title": "Card Title",
          "textStyle": { "fontSize": 18, "fontWeight": "bold" }
        },
        {
          "type": "Text",
          "description": "Card description",
          "modifier": { "paddingTop": 4 }
        },
        {
          "type": "Box",
          "modifier": {
            "fillMaxWidth": true,
            "contentAlignment": "centerend",
            "paddingTop": 12
          },
          "children": [
            {
              "type": "Button",
              "label": "Action",
              "action": "navigate:details"
            }
          ]
        }
      ]
    }
  ]
}
```

### Pattern 2: Horizontal Stats Row
```json
{
  "type": "Row",
  "modifier": {
    "fillMaxWidth": true,
    "verticalAlignment": "center"
  },
  "children": [
    {
      "type": "Card",
      "title": "42",
      "description": "Tasks",
      "elevation": 2,
      "modifier": { "fillMaxWidth": true, "paddingEnd": 8 }
    },
    {
      "type": "Card",
      "title": "18",
      "description": "Projects",
      "elevation": 2,
      "modifier": { "fillMaxWidth": true }
    }
  ]
}
```

### Pattern 3: Centered Content in Box
```json
{
  "type": "Box",
  "modifier": {
    "fillMaxWidth": true,
    "backgroundColor": "#ECEFF1",
    "padding": 24,
    "contentAlignment": "center"
  },
  "children": [
    {
      "type": "Column",
      "modifier": { "horizontalAlignment": "center" },
      "children": [
        {
          "type": "Text",
          "title": "Centered Title",
          "textStyle": { "fontSize": 18, "fontWeight": "medium" }
        },
        {
          "type": "Button",
          "label": "Action",
          "action": "navigate:somewhere",
          "modifier": { "paddingTop": 12 }
        }
      ]
    }
  ]
}
```

### Pattern 4: Full Screen with Scaffold
```json
{
  "type": "Scaffold",
  "topBar": {
    "type": "TopAppBar",
    "title": "Screen Title"
  },
  "content": {
    "type": "LazyColumn",
    "modifier": {
      "fillMaxSize": true,
      "padding": 16
    },
    "children": [
      // Your content items here
    ]
  }
}
```

## Important Rules

1. **Always use double quotes** for JSON strings, not single quotes
2. **Use LazyColumn for scrollable lists**, not Column
3. **Scaffold is for full screens**, use Column/Box for partial layouts
4. **Cards can have either title/description OR children**, not both together effectively (children will render in addition to title/description)
5. **Padding values are in dp** (device-independent pixels), typically 4, 8, 12, 16, 24, 32
6. **Elevation values** are typically 0-12 dp
7. **Font sizes** are in sp, typically 12, 14, 16, 18, 20, 24
8. **Colors must be hex format** with # prefix
9. **Actions are strings** that can be parsed by the app (e.g., "navigate:route")
10. **All boolean values** must be lowercase: `true` or `false`, not `True` or `False`

## Material Design Color Suggestions

Common Material Design 3 colors in hex:

**Blues:**
- Light: `#E3F2FD`, `#BBDEFB`
- Medium: `#2196F3`, `#1976D2`
- Dark: `#1565C0`, `#0D47A1`

**Greens:**
- Light: `#E8F5E9`, `#C8E6C9`
- Medium: `#4CAF50`, `#43A047`
- Dark: `#388E3C`, `#2E7D32`

**Reds:**
- Light: `#FFEBEE`, `#FFCDD2`
- Medium: `#F44336`, `#E53935`
- Dark: `#D32F2F`, `#C62828`

**Oranges:**
- Light: `#FFF3E0`, `#FFE0B2`
- Medium: `#FF9800`, `#FB8C00`
- Dark: `#F57C00`, `#EF6C00`

**Purples:**
- Light: `#F3E5F5`, `#E1BEE7`
- Medium: `#9C27B0`, `#8E24AA`
- Dark: `#7B1FA2`, `#6A1B9A`

**Grays:**
- Very Light: `#FAFAFA`, `#F5F5F5`
- Light: `#EEEEEE`, `#E0E0E0`
- Medium: `#9E9E9E`, `#757575`
- Dark: `#616161`, `#424242`
- Very Dark: `#212121`

## Response Format

When asked to create a UI, respond with:
1. A brief description of what you're creating
2. The complete JSON structure
3. Key features or notable design decisions

Example response:
"I'll create a dashboard screen with statistics cards and action buttons.

```json
{
  "type": "Scaffold",
  ...
}
```

This design includes:
- A top app bar with the title
- Statistics cards in a row layout
- Action buttons for common tasks
- Elevated cards for visual hierarchy"

## When User Requests UI

Ask yourself:
1. Is this a full screen? → Use Scaffold with LazyColumn content
2. Does it need to scroll? → Use LazyColumn
3. Should items be horizontal? → Use Row
4. Should items be vertical? → Use Column
5. Do items overlap/stack? → Use Box
6. Does it need visual separation? → Use Cards with elevation
7. Should content be centered? → Use appropriate alignment in modifier
8. Are there interactive elements? → Add Buttons with actions

Then generate the JSON accordingly, ensuring all properties are valid and colors are in hex format.
