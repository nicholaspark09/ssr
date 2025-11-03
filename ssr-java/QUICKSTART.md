# SSR Java - Quick Start Guide

## Running the Examples

The library includes several ready-to-run examples. Here's how to run them:

### 1. Build the module

```bash
./gradlew :ssr-java:compileJava
```

### 2. Run an example

```bash
# Find the GSON jar path (it will be downloaded by Gradle)
GSON_JAR=$(find ~/.gradle/caches/modules-2/files-2.1/com.google.code.gson -name "gson-2.10.1.jar" ! -name "*javadoc*" ! -name "*sources*" | head -1)

# Set the classpath
CLASSPATH="ssr-java/build/classes/java/main:$GSON_JAR"

# Run the simple table example
java -cp $CLASSPATH com.cincinnatiai.ssr_java.examples.SimpleTableExample

# Run the advanced table example
java -cp $CLASSPATH com.cincinnatiai.ssr_java.examples.AdvancedTableExample

# Run the card layout example
java -cp $CLASSPATH com.cincinnatiai.ssr_java.examples.CardLayoutExample
```

## Creating Your First UI

```java
import com.cincinnatiai.ssr_java.SSR;
import com.cincinnatiai.ssr_java.model.NodeModel;

public class MyFirstUI {
    public static void main(String[] args) {
        // Build a simple UI
        NodeModel ui = SSR.scaffold()
            .topBar(SSR.topAppBar("My App"))
            .content(
                SSR.column()
                    .modifier(SSR.modifier().padding(16))
                    .addChild(
                        SSR.text("Hello World!")
                            .textStyle(SSR.textStyle().fontSize(24).bold())
                    )
                    .addChild(
                        SSR.button("Click Me")
                            .action("button_clicked")
                            .modifier(SSR.modifier().paddingTop(16))
                    )
            )
            .build();

        // Convert to JSON
        String json = SSR.toJson(ui);
        
        System.out.println(json);
    }
}
```

## Common Patterns

### Creating a Table

```java
NodeModel table = SSR.table()
    .showBorders(true)
    .headerBackgroundColor("#6200EE")
    .addColumn(
        SSR.column("Name").weight(2.0f).horizontalAlignment("start")
    )
    .addColumn(
        SSR.column("Email").weight(3.0f).horizontalAlignment("start")
    )
    .addRow(
        SSR.cell("John Doe").build(),
        SSR.cell("john@example.com").build()
    )
    .build();
```

### Creating a Card

```java
NodeModel card = SSR.card()
    .elevation(4)
    .modifier(SSR.modifier().padding(16).fillMaxWidth())
    .addChild(
        SSR.column()
            .addChild(SSR.text("Card Title").textStyle(SSR.textStyle().fontSize(20).bold()))
            .addChild(SSR.text("Card content goes here"))
    )
    .build();
```

### Nested Layouts

```java
NodeModel layout = SSR.column()
    .addChild(
        SSR.row()
            .addChild(
                SSR.text("Left")
                    .modifier(SSR.modifier().weight(1.0f))
            )
            .addChild(
                SSR.text("Right")
                    .modifier(SSR.modifier().weight(1.0f))
            )
    )
    .build();
```

## Using in a Backend Service

### Spring Boot Example

```java
@RestController
@RequestMapping("/api/ui")
public class UIController {

    @GetMapping("/dashboard")
    public String getDashboard() {
        NodeModel dashboard = SSR.scaffold()
            .topBar(SSR.topAppBar("Dashboard"))
            .content(createDashboardContent())
            .build();

        return SSR.toJson(dashboard);
    }

    private NodeModel createDashboardContent() {
        return SSR.column()
            .modifier(SSR.modifier().padding(16))
            .addChild(SSR.text("Welcome!").textStyle(SSR.textStyle().fontSize(24).bold()))
            .addChild(SSR.text("Your dashboard content here"))
            .build();
    }
}
```

### JAX-RS Example

```java
@Path("/ui")
public class UIResource {

    @GET
    @Path("/profile")
    @Produces(MediaType.APPLICATION_JSON)
    public String getProfile(@QueryParam("userId") String userId) {
        // Fetch user data from database
        User user = userService.getUser(userId);

        // Build UI
        NodeModel profile = SSR.scaffold()
            .topBar(SSR.topAppBar("Profile"))
            .content(createProfileContent(user))
            .build();

        return SSR.toJson(profile);
    }

    private NodeModel createProfileContent(User user) {
        return SSR.column()
            .modifier(SSR.modifier().padding(16))
            .addChild(
                SSR.text(user.getName())
                    .textStyle(SSR.textStyle().fontSize(24).bold())
            )
            .addChild(
                SSR.text(user.getEmail())
                    .modifier(SSR.modifier().paddingTop(8))
            )
            .build();
    }
}
```

## Next Steps

1. Check out the `examples` package for more comprehensive examples
2. Read the full [README.md](README.md) for detailed API documentation
3. Integrate with your Android app using the SSR Simple library to render the JSON
