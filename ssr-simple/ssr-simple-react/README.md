# SSR-Simple React

A lightweight React/TypeScript library that converts JSON into native Material-UI components. This is the React equivalent of the ssr-simple Android library, allowing you to use the same JSON format across both platforms.

## Features

### Supported Components

- **Scaffold** - App layout with top bar, FAB, and content area
- **TopAppBar** - Application top bar with title
- **Column** - Vertical layout container with alignment support
- **Row** - Horizontal layout container with alignment support
- **Box** - Stacked layout container with content alignment
- **Card** - Material Design card with customizable elevation
- **Text** - Text display with custom styling
- **Button** - Interactive button with action handling
- **Image** - Image loading with size control
- **Table** - Data table with headers, flexible columns, cell styling, and click actions
- **Spacer** - Empty space for layout

## Installation

```bash
npm install ssr-simple-react
```

### Peer Dependencies

This library requires the following peer dependencies:

```bash
npm install @mui/material @mui/icons-material @emotion/react @emotion/styled react react-dom
```

## Usage

### Basic Usage

```tsx
import { SSRRenderer } from 'ssr-simple-react';

function App() {
  const jsonConfig = `{
    "type": "Card",
    "title": "Hello World",
    "description": "This card is rendered from JSON"
  }`;

  const handleAction = (action: string, data?: any) => {
    console.log('Action triggered:', action, data);
  };

  return <SSRRenderer jsonString={jsonConfig} onAction={handleAction} />;
}
```

### Using Node Model

You can also pass a parsed NodeModel directly:

```tsx
import { SSRRenderer, NodeModel } from 'ssr-simple-react';

const config: NodeModel = {
  type: 'Column',
  modifier: { padding: 16 },
  children: [
    {
      type: 'Text',
      title: 'Welcome',
      textStyle: { fontSize: 24, fontWeight: 'bold' }
    },
    {
      type: 'Button',
      label: 'Click Me',
      action: 'button_clicked'
    }
  ]
};

function App() {
  return <SSRRenderer nodeModel={config} onAction={(action) => alert(action)} />;
}
```

## Table Component

The Table component is one of the most powerful features, allowing you to create rich, interactive tables with minimal code.

### Example: Employee Directory Table

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
        "fontSize": 16,
        "fontWeight": "bold"
      }
    },
    {
      "header": "Status",
      "weight": 1.0,
      "horizontalAlignment": "center",
      "headerStyle": {
        "color": "#FFFFFF",
        "fontSize": 16,
        "fontWeight": "bold"
      }
    }
  ],
  "tableData": [
    [
      { "text": "Alice Johnson" },
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

**Table Features:**
- **Flexible columns** - Use `weight` for proportional widths or `width` (px) for fixed sizes
- **Header styling** - Custom background colors and text styles for headers
- **Cell styling** - Override styles per cell with `backgroundColor` and `textStyle`
- **Click actions** - `rowAction` for entire row clicks, or `action` per cell
- **Borders** - Optional borders with `showBorders: true`
- **Alignment** - Per-column alignment: `"start"`, `"center"`, `"end"`

## JSON Schema

### NodeModel

```typescript
interface NodeModel {
  type: string;              // Component type (required)
  title?: string;            // Primary text/heading
  description?: string;      // Secondary text content
  label?: string;            // Button/input labels
  backgroundColor?: string;  // Background color (hex)
  elevation?: number;        // Card elevation
  imageUrl?: string;         // Image URL for Image component
  imageWidth?: number;       // Image width in px
  imageHeight?: number;      // Image height in px
  contentDescription?: string; // Accessibility description for images
  action?: string;           // Action identifier
  modifier?: ModifierModel;  // Layout and styling modifiers
  textStyle?: TextStyleModel; // Text styling
  topBar?: NodeModel;        // Scaffold top bar
  floatingActionButton?: NodeModel; // Scaffold FAB
  content?: NodeModel;       // Scaffold content
  children?: NodeModel[];    // Child components

  // Button-specific
  buttonVariant?: 'filled' | 'outlined' | 'text';

  // Table-specific
  columns?: TableColumnModel[];
  tableData?: TableCellModel[][];
  showBorders?: boolean;
  headerBackgroundColor?: string;
  rowAction?: string;
  roundedCorners?: number;
}
```

### TableColumnModel

```typescript
interface TableColumnModel {
  header: string;            // Column header text (required)
  weight?: number;           // Column width weight (flexible)
  width?: number;            // Fixed column width in px (overrides weight)
  horizontalAlignment?: 'start' | 'center' | 'end';
  textStyle?: TextStyleModel; // Style for column cells
  headerStyle?: TextStyleModel; // Style for header
}
```

### TableCellModel

```typescript
interface TableCellModel {
  text: string;              // Cell text content (required)
  textStyle?: TextStyleModel; // Override column style
  backgroundColor?: string;  // Cell background color (hex)
  action?: string;           // Click action for this cell
  modifier?: ModifierModel;  // Additional styling
  showBorder?: boolean;      // Override table border setting
}
```

### ModifierModel

```typescript
interface ModifierModel {
  padding?: number;          // Uniform padding (px)
  paddingTop?: number;       // Top padding (px)
  paddingBottom?: number;    // Bottom padding (px)
  paddingStart?: number;     // Start/left padding (px)
  paddingEnd?: number;       // End/right padding (px)
  fillMaxSize?: boolean;     // Fill entire space
  fillMaxWidth?: boolean;    // Fill width only
  weight?: number;           // Flex weight for Row/Column children
  backgroundColor?: string;  // Background color (hex)
  horizontalAlignment?: 'start' | 'center' | 'end'; // For Column
  verticalAlignment?: 'top' | 'center' | 'bottom';  // For Row
  contentAlignment?: string; // For Box
  verticalScroll?: boolean;  // Enable vertical scrolling
}
```

### TextStyleModel

```typescript
interface TextStyleModel {
  fontSize?: number;         // Font size in px
  fontWeight?: 'normal' | 'medium' | 'bold' | 'light';
  textAlign?: 'start' | 'center' | 'end' | 'left' | 'right';
  color?: string;            // Text color (hex)
}
```

## Examples

Sample JSON files are available in the `src/examples/` directory:
- `simple_table.json` - Basic table with employee directory
- `advanced_table.json` - Sales dashboard with styled cells and actions

## Migrating from Existing Material-UI Code

If you have existing Material-UI table code, you can convert it to use SSR-Simple React:

### Before (Traditional React):

```tsx
<Table>
  <TableHead>
    <TableRow>
      <TableCell>Name</TableCell>
      <TableCell>Status</TableCell>
    </TableRow>
  </TableHead>
  <TableBody>
    <TableRow>
      <TableCell>Alice</TableCell>
      <TableCell>Active</TableCell>
    </TableRow>
  </TableBody>
</Table>
```

### After (SSR-Simple React):

```tsx
<SSRRenderer
  nodeModel={{
    type: 'Table',
    columns: [
      { header: 'Name', weight: 1 },
      { header: 'Status', weight: 1 }
    ],
    tableData: [
      [
        { text: 'Alice' },
        { text: 'Active' }
      ]
    ]
  }}
/>
```

## Benefits

1. **Cross-Platform Consistency** - Use the same JSON format for Android (Jetpack Compose) and Web (React)
2. **Server-Side Rendering** - Generate UI from backend JSON responses
3. **Dynamic UIs** - Change UI without redeploying your app
4. **Reduced Code** - Less boilerplate compared to traditional React components
5. **Type-Safe** - Full TypeScript support with strong typing

## Building the Library

```bash
npm install
npm run build
```

This will generate the compiled JavaScript and TypeScript declaration files in the `dist/` directory.

## License

MIT
