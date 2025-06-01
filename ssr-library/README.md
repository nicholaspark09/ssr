# SSR Library

This is a Server-Side Rendering library for Android Jetpack Compose that allows you to create dynamic UI
screens using JSON configuration.

- This is a work in progress
- You **may** use a backend to send back the json **or** you can host your json files locally

## 📱 Quick Start

```kotlin
val renderer = ServerSideRendererImpl()
renderer.RenderScreen(
    json = yourJsonString,
    loadingContent = { CircularProgressIndicator() },
    errorContent = { message, retry -> ErrorScreen(message, retry) }
)
```

## 🎯 Basic JSON Structure

Every screen follows this basic structure:

```json
{
  "version": "1.0",
  "screen": {
    "id": "screen_id",
    "title": "Screen Title",
    "layout": {
      "type": "component_type",
      "modifier": {
        /* styling */
      },
      "properties": {
        /* component properties */
      },
      "children": [
        /* child components */
      ]
    }
  },
  "theme": {
    /* optional theme */
  }
}
```

## 🧩 Core Components

### Text Component

```json
{
  "type": "text",
  "properties": {
    "text": "Hello World",
    "style": "headline1"
  },
  "modifier": {
    "padding": 16
  }
}
```

**Text Styles:** `headline1`, `headline2`, `subtitle1`, `body1`, `body2`

### Button Component

```json
{
  "type": "button",
  "properties": {
    "text": "Click Me",
    "enabled": true
  },
  "modifier": {
    "fillMaxWidth": true
  },
  "actions": {
    "onClick": {
      "type": "navigation",
      "destination": "next_screen"
    }
  }
}
```

### Layout Components

#### Column

```json
{
  "type": "column",
  "modifier": {
    "fillMaxSize": true,
    "padding": 16
  },
  "children": [
    {
      /* child components */
    }
  ]
}
```

#### Row

```json
{
  "type": "row",
  "modifier": {
    "fillMaxWidth": true
  },
  "children": [
    {
      "type": "text",
      "properties": {
        "text": "Left"
      },
      "modifier": {
        "weight": 1
      }
    },
    {
      "type": "text",
      "properties": {
        "text": "Right"
      }
    }
  ]
}
```

### Card Component

```json
{
  "type": "card",
  "modifier": {
    "fillMaxWidth": true,
    "padding": 8
  },
  "children": [
    {
      "type": "column",
      "modifier": {
        "padding": 16
      },
      "children": [
        {
          "type": "text",
          "properties": {
            "text": "Card Title",
            "style": "headline2"
          }
        },
        {
          "type": "text",
          "properties": {
            "text": "Card content goes here",
            "style": "body1"
          }
        }
      ]
    }
  ]
}
```

### Image Component

```json
{
  "type": "image",
  "properties": {
    "url": "https://example.com/image.jpg",
    "contentDescription": "Description"
  },
  "modifier": {
    "width": 100,
    "height": 100
  }
}
```

### Spacer Component

```json
{
  "type": "spacer",
  "properties": {
    "height": 16,
    "width": 8
  }
}
```

## 🎨 Modifiers

Common modifier properties:

```json
{
  "modifier": {
    "padding": 16,
    "fillMaxSize": true,
    "fillMaxWidth": true,
    "width": 200,
    "height": 100,
    "weight": 1.0
  }
}
```

- `padding`: Add padding around component
- `fillMaxSize`: Fill entire available space
- `fillMaxWidth`: Fill available width
- `width`/`height`: Fixed dimensions in dp
- `weight`: Proportional sizing in Column/Row

## 📜 Lists and Data

### Static LazyColumn

```json
{
  "type": "lazy_column",
  "modifier": {
    "fillMaxWidth": true,
    "weight": 1
  },
  "dataSource": {
    "type": "static",
    "items": [
      {
        "id": "1",
        "title": "Item 1",
        "description": "First item"
      },
      {
        "id": "2",
        "title": "Item 2",
        "description": "Second item"
      }
    ]
  },
  "itemTemplate": {
    "type": "card",
    "layout": {
      "type": "column",
      "modifier": {
        "padding": 16
      },
      "children": [
        {
          "type": "text",
          "properties": {
            "text": "{{item.title}}",
            "style": "headline2"
          }
        },
        {
          "type": "text",
          "properties": {
            "text": "{{item.description}}",
            "style": "body1"
          }
        }
      ]
    },
    "actions": {
      "onClick": {
        "type": "navigation",
        "destination": "item_detail",
        "params": {
          "itemId": "{{item.id}}"
        }
      }
    }
  }
}
```

### Data Binding

Use `{{item.property}}` syntax to bind data from list items:

- `{{item.title}}` - Simple property
- `{{item.user.name}}` - Nested property
- `${{item.price}}` - With prefix/suffix

## 🎭 Actions

### Navigation Action

```json
{
  "actions": {
    "onClick": {
      "type": "navigation",
      "destination": "target_screen",
      "params": {
        "userId": "123",
        "section": "profile"
      }
    }
  }
}
```

### Generic Action

```json
{
  "actions": {
    "onClick": {
      "type": "action",
      "destination": "show_dialog"
    }
  }
}
```

## 📺 Screen Examples

### 1. Simple Home Screen

```json
{
  "version": "1.0",
  "screen": {
    "id": "home_screen",
    "title": "Home",
    "layout": {
      "type": "column",
      "modifier": {
        "fillMaxSize": true,
        "padding": 16
      },
      "children": [
        {
          "type": "top_app_bar",
          "properties": {
            "title": "My App",
            "centerTitle": true,
            "backgroundColor": "#FFFFFF",
            "titleColor": "#000000"
          }
        },
        {
          "type": "text",
          "properties": {
            "text": "Welcome Home!",
            "style": "headline1"
          },
          "modifier": {
            "padding": 16
          }
        },
        {
          "type": "card",
          "modifier": {
            "fillMaxWidth": true,
            "padding": 8
          },
          "children": [
            {
              "type": "column",
              "modifier": {
                "padding": 16
              },
              "children": [
                {
                  "type": "text",
                  "properties": {
                    "text": "Quick Actions",
                    "style": "headline2"
                  }
                },
                {
                  "type": "spacer",
                  "properties": {
                    "height": 16
                  }
                },
                {
                  "type": "button",
                  "properties": {
                    "text": "Get Started",
                    "enabled": true
                  },
                  "modifier": {
                    "fillMaxWidth": true
                  },
                  "actions": {
                    "onClick": {
                      "type": "navigation",
                      "destination": "onboarding"
                    }
                  }
                }
              ]
            }
          ]
        }
      ]
    }
  }
}
```

### 2. List Screen

```json
{
  "version": "1.0",
  "screen": {
    "id": "product_list",
    "title": "Products",
    "layout": {
      "type": "column",
      "modifier": {
        "fillMaxSize": true
      },
      "children": [
        {
          "type": "top_app_bar",
          "properties": {
            "title": "Products",
            "centerTitle": true
          }
        },
        {
          "type": "lazy_column",
          "modifier": {
            "fillMaxWidth": true,
            "weight": 1
          },
          "dataSource": {
            "type": "static",
            "items": [
              {
                "id": "1",
                "name": "Smartphone",
                "price": 599,
                "description": "Latest model",
                "imageUrl": "https://example.com/phone.jpg"
              },
              {
                "id": "2",
                "name": "Laptop",
                "price": 999,
                "description": "High performance",
                "imageUrl": "https://example.com/laptop.jpg"
              }
            ]
          },
          "itemTemplate": {
            "type": "card",
            "layout": {
              "type": "row",
              "modifier": {
                "fillMaxWidth": true,
                "padding": 16
              },
              "children": [
                {
                  "type": "image",
                  "properties": {
                    "url": "{{item.imageUrl}}",
                    "contentDescription": "{{item.name}}"
                  },
                  "modifier": {
                    "width": 80,
                    "height": 80
                  }
                },
                {
                  "type": "spacer",
                  "properties": {
                    "width": 16
                  }
                },
                {
                  "type": "column",
                  "modifier": {
                    "weight": 1
                  },
                  "children": [
                    {
                      "type": "text",
                      "properties": {
                        "text": "{{item.name}}",
                        "style": "headline2"
                      }
                    },
                    {
                      "type": "text",
                      "properties": {
                        "text": "{{item.description}}",
                        "style": "body2"
                      }
                    },
                    {
                      "type": "text",
                      "properties": {
                        "text": "${{item.price}}",
                        "style": "subtitle1"
                      }
                    }
                  ]
                }
              ]
            },
            "actions": {
              "onClick": {
                "type": "navigation",
                "destination": "product_detail",
                "params": {
                  "productId": "{{item.id}}"
                }
              }
            }
          }
        }
      ]
    }
  }
}
```

### 3. Detail Screen

```json
{
  "version": "1.0",
  "screen": {
    "id": "product_detail",
    "title": "Product Detail",
    "layout": {
      "type": "column",
      "modifier": {
        "fillMaxSize": true
      },
      "children": [
        {
          "type": "top_app_bar",
          "properties": {
            "title": "Product Detail",
            "centerTitle": false
          }
        },
        {
          "type": "column",
          "modifier": {
            "weight": 1,
            "padding": 16
          },
          "children": [
            {
              "type": "image",
              "properties": {
                "url": "https://example.com/product.jpg",
                "contentDescription": "Product image"
              },
              "modifier": {
                "fillMaxWidth": true,
                "height": 200
              }
            },
            {
              "type": "spacer",
              "properties": {
                "height": 16
              }
            },
            {
              "type": "text",
              "properties": {
                "text": "Premium Smartphone",
                "style": "headline1"
              }
            },
            {
              "type": "text",
              "properties": {
                "text": "$599",
                "style": "headline2"
              },
              "modifier": {
                "padding": 8
              }
            },
            {
              "type": "text",
              "properties": {
                "text": "This is a detailed description of the product with all its features and specifications.",
                "style": "body1"
              },
              "modifier": {
                "padding": 8
              }
            },
            {
              "type": "spacer",
              "properties": {
                "height": 24
              }
            },
            {
              "type": "row",
              "modifier": {
                "fillMaxWidth": true
              },
              "children": [
                {
                  "type": "button",
                  "properties": {
                    "text": "Add to Cart",
                    "enabled": true
                  },
                  "modifier": {
                    "weight": 1
                  },
                  "actions": {
                    "onClick": {
                      "type": "action",
                      "destination": "add_to_cart"
                    }
                  }
                },
                {
                  "type": "spacer",
                  "properties": {
                    "width": 8
                  }
                },
                {
                  "type": "button",
                  "properties": {
                    "text": "Buy Now",
                    "enabled": true
                  },
                  "modifier": {
                    "weight": 1
                  },
                  "actions": {
                    "onClick": {
                      "type": "action",
                      "destination": "buy_now"
                    }
                  }
                }
              ]
            }
          ]
        }
      ]
    }
  }
}
```

### 4. Settings Screen

```json
{
  "version": "1.0",
  "screen": {
    "id": "settings_screen",
    "title": "Settings",
    "layout": {
      "type": "column",
      "modifier": {
        "fillMaxSize": true
      },
      "children": [
        {
          "type": "top_app_bar",
          "properties": {
            "title": "Settings",
            "centerTitle": true
          }
        },
        {
          "type": "lazy_column",
          "modifier": {
            "fillMaxWidth": true,
            "weight": 1
          },
          "dataSource": {
            "type": "static",
            "items": [
              {
                "id": "profile",
                "title": "Profile",
                "subtitle": "Manage your profile information",
                "icon": "person"
              },
              {
                "id": "notifications",
                "title": "Notifications",
                "subtitle": "Push notifications and alerts",
                "icon": "notifications"
              },
              {
                "id": "privacy",
                "title": "Privacy",
                "subtitle": "Privacy settings and data",
                "icon": "security"
              },
              {
                "id": "help",
                "title": "Help & Support",
                "subtitle": "Get help and contact support",
                "icon": "help"
              }
            ]
          },
          "itemTemplate": {
            "type": "card",
            "layout": {
              "type": "row",
              "modifier": {
                "fillMaxWidth": true,
                "padding": 16
              },
              "children": [
                {
                  "type": "column",
                  "modifier": {
                    "weight": 1
                  },
                  "children": [
                    {
                      "type": "text",
                      "properties": {
                        "text": "{{item.title}}",
                        "style": "subtitle1"
                      }
                    },
                    {
                      "type": "text",
                      "properties": {
                        "text": "{{item.subtitle}}",
                        "style": "body2"
                      }
                    }
                  ]
                },
                {
                  "type": "text",
                  "properties": {
                    "text": ">",
                    "style": "body1"
                  }
                }
              ]
            },
            "actions": {
              "onClick": {
                "type": "navigation",
                "destination": "{{item.id}}_settings",
                "params": {
                  "settingType": "{{item.id}}"
                }
              }
            }
          }
        }
      ]
    }
  }
}
```

## 🎨 Theming

Add custom themes to your screens:

```json
{
  "theme": {
    "primaryColor": "#6200EE",
    "secondaryColor": "#03DAC5",
    "backgroundColor": "#FFFFFF",
    "textColor": "#000000"
  }
}
```

**Color Formats:**

- Hex: `#FF0000`, `#FFFFFF`
- Short hex: `#F00`, `#FFF`
- Named: `red`, `white`, `black`, `blue`

## 🏗️ App Bar

### Centered App Bar

```json
{
  "type": "top_app_bar",
  "properties": {
    "title": "My App",
    "centerTitle": true,
    "backgroundColor": "#FFFFFF",
    "titleColor": "#000000"
  }
}
```

### Left-Aligned App Bar

```json
{
  "type": "top_app_bar",
  "properties": {
    "title": "My App",
    "centerTitle": false
  }
}
```

## 📱 Best Practices

### 1. Screen Layout Structure

```
Column (fillMaxSize)
├── TopAppBar (fixed)
├── Content Area (weight: 1)
└── Bottom Navigation (optional, fixed)
```

### 2. Scrollable Content

Always use `weight: 1` on scrollable content areas:

```json
{
  "type": "lazy_column",
  "modifier": {
    "fillMaxWidth": true,
    "weight": 1
  }
}
```

### 3. Responsive Layouts

Use `weight` for proportional sizing:

```json
{
  "type": "row",
  "children": [
    {
      "type": "text",
      "properties": {
        "text": "Left"
      },
      "modifier": {
        "weight": 2
      }
    },
    {
      "type": "text",
      "properties": {
        "text": "Right"
      },
      "modifier": {
        "weight": 1
      }
    }
  ]
}
```

### 4. Consistent Spacing

Use consistent padding and spacer values:

- Small: 8dp
- Medium: 16dp
- Large: 24dp

## 🚀 Getting Started

1. **Add the library** to your project
2. **Create a JSON screen** using the examples above
3. **Render it** with `ServerSideRendererImpl`
4. **Handle actions** with custom `ActionHandler`
5. **Test and iterate** with different layouts

## 📚 Component Reference

| Component     | Purpose            | Required Properties          |
|---------------|--------------------|------------------------------|
| `text`        | Display text       | `text`                       |
| `button`      | Interactive button | `text`                       |
| `image`       | Display images     | `url`                        |
| `column`      | Vertical layout    | -                            |
| `row`         | Horizontal layout  | -                            |
| `card`        | Material card      | -                            |
| `spacer`      | Add spacing        | `height` or `width`          |
| `lazy_column` | Scrollable list    | `dataSource`, `itemTemplate` |
| `top_app_bar` | App bar            | `title`                      |

Happy building! 🎉