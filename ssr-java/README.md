# SSR Java - Server-Side Rendering Library for Java

A pure Java library for building UI component definitions in JSON format. This library allows you to construct UI layouts programmatically on the backend without any Android or Jetpack Compose dependencies.

## Features

- Pure Java implementation (Java 11+)
- No Android dependencies
- Fluent builder API for easy UI construction
- JSON serialization/deserialization support
- Support for Tables, Scaffolds, Columns, Rows, Cards, and more
- Compatible with the SSR Simple renderer on Android

## Installation

### Gradle

```gradle
dependencies {
    implementation 'com.cincinnatiai:ssr-java:0.0.1'
}
```

### Maven

```xml
<dependency>
    <groupId>com.cincinnatiai</groupId>
    <artifactId>ssr-java</artifactId>
    <version>0.0.1</version>
</dependency>
```

## Quick Start

```java
import com.cincinnatiai.ssr_java.SSR;
import com.cincinnatiai.ssr_java.model.NodeModel;

// Create a simple UI
NodeModel ui = SSR.scaffold()
    .topBar(SSR.topAppBar("My App"))
    .content(
        SSR.column()
            .modifier(SSR.modifier().padding(16))
            .addChild(
                SSR.text("Hello World!")
                    .textStyle(SSR.textStyle().fontSize(24).bold())
            )
    )
    .build();

// Serialize to JSON
String json = SSR.toJson(ui);
System.out.println(json);
```

## Components

### Layouts

- **Scaffold**: Main screen layout with top bar, content, and FAB
- **Column**: Vertical layout
- **Row**: Horizontal layout
- **Box**: Stack-based layout

### UI Elements

- **Text**: Display text with styling
- **Button**: Clickable button with variants (filled, outlined, text)
- **Card**: Material Design card container
- **Image**: Display images from URL
- **Table**: Data table with customizable columns and cells
- **Spacer**: Add spacing between elements
- **Divider**: Horizontal divider line

### Modifiers

- Padding (all sides or individual)
- Size (width, height, weight)
- Fill max size/width
- Background color
- Alignment (horizontal, vertical, content)
- Vertical scroll

### Text Styles

- Font size
- Font weight
- Text alignment
- Color

## Examples

### Simple Table

```java
NodeModel ui = SSR.scaffold()
    .topBar(SSR.topAppBar("Employee Directory"))
    .content(
        SSR.column()
            .modifier(SSR.modifier().padding(16))
            .addChild(
                SSR.text("Employee Directory")
                    .textStyle(SSR.textStyle().fontSize(24).bold())
                    .modifier(SSR.modifier().paddingBottom(16))
            )
            .addChild(
                SSR.table()
                    .showBorders(true)
                    .headerBackgroundColor("#6200EE")
                    .addColumn(
                        SSR.column("Name")
                            .weight(2.0f)
                            .horizontalAlignment("start")
                    )
                    .addColumn(
                        SSR.column("Role")
                            .weight(1.5f)
                            .horizontalAlignment("start")
                    )
                    .addColumn(
                        SSR.column("Status")
                            .weight(1.0f)
                            .horizontalAlignment("center")
                    )
                    .addRow(
                        SSR.cell("Alice Johnson").build(),
                        SSR.cell("Engineer").build(),
                        SSR.cell("Active")
                            .backgroundColor("#4CAF50")
                            .textStyle(SSR.textStyle().color("#FFFFFF").bold())
                            .build()
                    )
                    .addRow(
                        SSR.cell("Bob Smith").build(),
                        SSR.cell("Designer").build(),
                        SSR.cell("Active")
                            .backgroundColor("#4CAF50")
                            .textStyle(SSR.textStyle().color("#FFFFFF").bold())
                            .build()
                    )
            )
    )
    .build();

String json = SSR.toJson(ui);
```

### Card Layout

```java
NodeModel card = SSR.card()
    .modifier(
        SSR.modifier()
            .padding(16)
            .fillMaxWidth()
    )
    .addChild(
        SSR.column()
            .addChild(
                SSR.text("Card Title")
                    .textStyle(SSR.textStyle().fontSize(20).bold())
            )
            .addChild(
                SSR.text("Card description goes here")
                    .modifier(SSR.modifier().paddingTop(8))
            )
            .addChild(
                SSR.button("Action")
                    .action("button_clicked")
                    .modifier(SSR.modifier().paddingTop(16))
            )
    )
    .build();
```

### Interactive Table with Actions

```java
NodeModel table = SSR.table()
    .showBorders(true)
    .headerBackgroundColor("#1976D2")
    .rowAction("row_clicked")
    .modifier(SSR.modifier().fillMaxWidth())
    .addColumn(
        SSR.column("Product")
            .weight(2.0f)
            .headerStyle(SSR.textStyle().color("#FFFFFF").fontSize(16))
    )
    .addColumn(
        SSR.column("Action")
            .weight(1.0f)
            .headerStyle(SSR.textStyle().color("#FFFFFF").fontSize(16))
    )
    .addRow(
        SSR.cell("Laptop Pro").build(),
        SSR.cell("View")
            .action("view_details")
            .backgroundColor("#2196F3")
            .textStyle(SSR.textStyle().color("#FFFFFF").bold())
            .build()
    )
    .build();
```

## Advanced Usage

### Using Direct Model Construction

If you prefer not to use builders, you can construct models directly:

```java
NodeModel node = new NodeModel();
node.setType("Text");
node.setTitle("Hello World");

TextStyleModel style = new TextStyleModel();
style.setFontSize(24f);
style.setFontWeight("bold");
node.setTextStyle(style);
```

### JSON Serialization Options

```java
// Pretty-printed JSON (default)
String prettyJson = SSR.toJson(node);

// Compact JSON
String compactJson = SSR.toJsonCompact(node);

// Deserialize from JSON
NodeModel fromJson = SSR.fromJson(jsonString);
```

## API Reference

### SSR Static Methods

Factory methods for creating components:

- `scaffold()` - Create a Scaffold layout
- `topAppBar(String title)` - Create a TopAppBar
- `column()` - Create a Column layout
- `row()` - Create a Row layout
- `box()` - Create a Box layout
- `text(String title)` - Create a Text component
- `button(String label)` - Create a Button
- `card()` - Create a Card
- `image(String url)` - Create an Image
- `table()` - Create a Table
- `spacer(int height)` - Create a Spacer
- `divider()` - Create a Divider

Utility methods:

- `column(String header)` - Create a TableColumnBuilder
- `cell(String text)` - Create a TableCellBuilder
- `modifier()` - Create a ModifierBuilder
- `textStyle()` - Create a TextStyleBuilder
- `toJson(NodeModel)` - Serialize to pretty JSON
- `toJsonCompact(NodeModel)` - Serialize to compact JSON
- `fromJson(String)` - Deserialize from JSON

## Integration

This library generates JSON that can be consumed by the SSR Simple library on Android to render actual Compose UI components. Simply send the JSON from your backend to your Android client and use the SSR Simple library to render it.

## License

MIT License
