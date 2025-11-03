# VideoItem and HorizontalPager Support

This document describes the newly added support for VideoItem and HorizontalPager components in the ssr-simple golang package.

## VideoItem

VideoItem is a card-like component designed to display video content with a thumbnail, title, and description.

### Functions

#### VideoItem
Creates a video item with full customization options.

```go
func VideoItem(
    title, description, imageURL, action string,
    elevation, roundedCorners float64,
    backgroundColor string,
    modifier *ModifierModel
) *NodeModel
```

**Parameters:**
- `title`: Video title
- `description`: Video description
- `imageURL`: URL to the video thumbnail image
- `action`: Action to trigger when clicked (e.g., "play:video_001")
- `elevation`: Card elevation in dp
- `roundedCorners`: Corner radius in dp
- `backgroundColor`: Background color (hex format, e.g., "#E3F2FD")
- `modifier`: Layout modifier

**Example:**
```go
videoItem := ssr.VideoItem(
    "Advanced Go Patterns",
    "Deep dive into advanced Go patterns and best practices.",
    "https://example.com/thumbnail.jpg",
    "play:video_002",
    8.0,   // elevation
    16.0,  // rounded corners
    "#E3F2FD", // background color
    ssr.Modifier().WithFillMaxWidth().WithPadding(16),
)
```

#### VideoItemSimple
Creates a video item with default styling (elevation: 4dp, rounded corners: 12dp).

```go
func VideoItemSimple(
    title, description, imageURL, action string,
    modifier *ModifierModel
) *NodeModel
```

**Example:**
```go
videoItem := ssr.VideoItemSimple(
    "Introduction to Go",
    "Learn the basics of Go programming.",
    "https://example.com/thumbnail.jpg",
    "play:video_001",
    ssr.Modifier().WithFillMaxWidth().WithPadding(16),
)
```

## HorizontalPager

HorizontalPager creates a horizontal swipeable pager component that can contain any type of child components.

### Function

```go
func HorizontalPager(children []NodeModel, modifier *ModifierModel) *NodeModel
```

**Parameters:**
- `children`: Array of NodeModel elements to display in the pager
- `modifier`: Layout modifier

**Example:**
```go
pager := ssr.HorizontalPager(
    []ssr.NodeModel{
        *ssr.VideoItemSimple("Video 1", "Description 1", "url1", "action1", nil),
        *ssr.VideoItemSimple("Video 2", "Description 2", "url2", "action2", nil),
        *ssr.VideoItemSimple("Video 3", "Description 3", "url3", "action3", nil),
    },
    ssr.Modifier().WithFillMaxWidth().WithHeight(300),
)
```

## Complete Example

Here's a complete example combining VideoItem and HorizontalPager:

```go
package main

import (
    "fmt"
    "log"
    ssr "path/to/your/module/ssr_simple"
)

func main() {
    screen := ssr.Scaffold(
        "Video Library",
        ssr.Column(
            []ssr.NodeModel{
                *ssr.Text(
                    "Featured Videos",
                    ssr.HeadingStyle(),
                    ssr.Modifier().WithPadding(16),
                ),
                *ssr.HorizontalPager(
                    []ssr.NodeModel{
                        *ssr.VideoItem(
                            "Video 1: Introduction",
                            "Learn the basics in this introductory video.",
                            "https://example.com/thumb1.jpg",
                            "play:video_001",
                            4.0,
                            12.0,
                            "#E3F2FD",
                            ssr.Modifier().WithFillMaxWidth().WithPadding(16),
                        ),
                        *ssr.VideoItem(
                            "Video 2: Advanced Topics",
                            "Dive deeper into advanced topics.",
                            "https://example.com/thumb2.jpg",
                            "play:video_002",
                            4.0,
                            12.0,
                            "#F3E5F5",
                            ssr.Modifier().WithFillMaxWidth().WithPadding(16),
                        ),
                    },
                    ssr.Modifier().WithFillMaxWidth().WithHeight(200),
                ),
            },
            ssr.Modifier().WithFillMaxSize(),
        ),
    )

    json, err := screen.ToJSONIndent()
    if err != nil {
        log.Fatal(err)
    }
    fmt.Println(json)
}
```

## Layout Details

### VideoItem Layout
- Row layout with content on the left and thumbnail on the right
- Title and description in a Column on the left
- Thumbnail image (120dp) on the right with 8dp rounded corners
- Supports click actions via the `action` parameter
- Customizable background color, elevation, and corner radius

### HorizontalPager Layout
- Horizontal swipeable container
- Can contain any type of components (VideoItem, Card, Column, etc.)
- Supports all standard modifiers (width, height, padding, etc.)
- Users can swipe left/right to navigate between pages

## Testing

Run the tests to verify functionality:

```bash
cd ssr-simple/golang
go test -v
```

All tests should pass, including:
- `TestVideoItem` - Tests full VideoItem creation
- `TestVideoItemSimple` - Tests VideoItemSimple with defaults
- `TestHorizontalPager` - Tests HorizontalPager with VideoItem children
- `TestHorizontalPagerWithMixedContent` - Tests HorizontalPager with mixed content types

## Examples

See the complete working examples in:
- `examples/video_pager_example.go` - Comprehensive examples of VideoItem and HorizontalPager usage
