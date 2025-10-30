# SSR Java Module

A pure Java backend library for building SSR (Server-Side Rendering) JSON UI definitions.

## Module Overview

**Module Name:** `ssr-java`
**Package:** `com.cincinnatiai.ssr_java`
**Java Version:** 11+
**Dependencies:** Gson 2.10.1 (for JSON serialization)

## Key Features

- Pure Java implementation with NO Android or Jetpack Compose dependencies
- Can run on any Java backend (Spring Boot, JAX-RS, servlets, etc.)
- Fluent builder API for easy UI construction
- Automatic JSON serialization/deserialization
- Compatible with SSR Simple library on Android for rendering

## Module Structure

```
ssr-java/
├── src/main/java/com/cincinnatiai/ssr_java/
│   ├── SSR.java                          # Main entry point with factory methods
│   ├── model/                             # Data models
│   │   ├── NodeModel.java                 # Main UI node model
│   │   ├── ModifierModel.java             # Layout modifiers
│   │   ├── TextStyleModel.java            # Text styling
│   │   ├── TableCellModel.java            # Table cell data
│   │   └── TableColumnModel.java          # Table column definition
│   ├── builder/                           # Fluent builders
│   │   ├── NodeBuilder.java               # UI component builder
│   │   ├── ModifierBuilder.java           # Modifier builder
│   │   ├── TextStyleBuilder.java          # Text style builder
│   │   ├── TableCellBuilder.java          # Table cell builder
│   │   └── TableColumnBuilder.java        # Table column builder
│   ├── util/                              # Utilities
│   │   └── JsonSerializer.java            # JSON serialization
│   └── examples/                          # Example code
│       ├── SimpleTableExample.java        # Simple table demo
│       ├── AdvancedTableExample.java      # Advanced table with actions
│       └── CardLayoutExample.java         # Card layout demo
├── build.gradle.kts                       # Build configuration
├── README.md                              # Full documentation
├── QUICKSTART.md                          # Quick start guide
└── MODULE_INFO.md                         # This file

```

## Supported Components

### Layouts
- **Scaffold** - Main screen layout with top bar, content, FAB
- **Column** - Vertical linear layout
- **Row** - Horizontal linear layout
- **Box** - Stack/overlay layout
- **Card** - Material Design card

### UI Elements
- **Text** - Text display with styling
- **Button** - Clickable button (filled, outlined, text variants)
- **Image** - Image from URL
- **Table** - Data table with columns and cells
- **Spacer** - Empty space
- **Divider** - Horizontal line
- **TopAppBar** - Top navigation bar

### Modifiers
- Padding (all sides or individual)
- Size (width, height, weight)
- Fill max size/width
- Background color
- Alignment (horizontal, vertical, content)
- Vertical scroll

### Text Styles
- Font size
- Font weight (normal, bold)
- Text alignment
- Color (hex format)

## Usage Example

```java
import com.cincinnatiai.ssr_java.SSR;
import com.cincinnatiai.ssr_java.model.NodeModel;

public class Example {
    public static void main(String[] args) {
        NodeModel ui = SSR.scaffold()
            .topBar(SSR.topAppBar("My App"))
            .content(
                SSR.column()
                    .modifier(SSR.modifier().padding(16))
                    .addChild(
                        SSR.text("Hello from Java!")
                            .textStyle(SSR.textStyle().fontSize(24).bold())
                    )
            )
            .build();

        String json = SSR.toJson(ui);
        // Send this JSON to your Android app
        System.out.println(json);
    }
}
```

## Building the Module

```bash
# Compile
./gradlew :ssr-java:compileJava

# Build JAR
./gradlew :ssr-java:jar

# Generate sources and javadoc JARs
./gradlew :ssr-java:sourcesJar :ssr-java:javadocJar

# Publish to Maven repository
./gradlew :ssr-java:publish
```

## Integration with Android

1. **Backend (Java):** Use this library to generate JSON UI definitions
2. **Transport:** Send JSON via REST API, WebSocket, or any transport mechanism
3. **Android Client:** Use SSR Simple library to parse JSON and render Compose UI

```
┌─────────────┐         ┌──────────────┐         ┌─────────────┐
│   Backend   │  JSON   │   Network    │  JSON   │   Android   │
│  (ssr-java) │ ──────> │   Transport  │ ──────> │(ssr-simple) │
│             │         │              │         │   Compose   │
└─────────────┘         └──────────────┘         └─────────────┘
```

## Version History

- **0.0.1** - Initial release
  - Core model classes
  - Fluent builder API
  - JSON serialization
  - Table support
  - Example implementations

## License

MIT License

## Author

Nicholas Park (nicholaspark09@gmail.com)
