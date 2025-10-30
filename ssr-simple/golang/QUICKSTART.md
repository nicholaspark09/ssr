# SSR-Simple Go Quick Start Guide

## Setup

### Option 1: Copy into your project

1. Copy `ssr_simple.go` into your project directory
2. Update the package name if needed
3. Import and use:

```go
import ssr "yourproject/ssr_simple"
```

### Option 2: Use as a module

1. Initialize your Go module if you haven't:
```bash
go mod init yourproject
```

2. Copy the entire `golang/` directory into your project
3. Import:
```go
import ssr "yourproject/golang/ssr_simple"
```

## 5-Minute Integration

### 1. Basic HTTP Endpoint

Create a file `main.go`:

```go
package main

import (
    "encoding/json"
    "net/http"

    ssr "yourproject/ssr_simple"
)

func main() {
    http.HandleFunc("/api/screen", handleScreen)
    http.ListenAndServe(":8080", nil)
}

func handleScreen(w http.ResponseWriter, r *http.Request) {
    screen := ssr.Scaffold(
        "My App",
        ssr.Column(
            []ssr.NodeModel{
                *ssr.Text("Hello from Go!", ssr.HeadingStyle(), nil),
                *ssr.Card("Welcome", "Server-driven UI", 4.0, nil),
                *ssr.Button("Click Me", "action:hello", nil),
            },
            ssr.Modifier().WithPadding(16),
        ),
    )

    w.Header().Set("Content-Type", "application/json")
    json.NewEncoder(w).Encode(screen)
}
```

Run:
```bash
go run main.go
```

Test:
```bash
curl http://localhost:8080/api/screen
```

### 2. Dynamic Dashboard from Database

```go
type Stats struct {
    Users    int
    Orders   int
    Revenue  string
}

func getDashboard(w http.ResponseWriter, r *http.Request) {
    // Fetch from your database
    stats := fetchStats() // Returns Stats

    screen := ssr.Scaffold(
        "Dashboard",
        ssr.LazyColumn(
            []ssr.NodeModel{
                *ssr.Text(
                    "Today's Stats",
                    ssr.HeadingStyle(),
                    ssr.Modifier().WithPaddingBottom(16),
                ),
                *ssr.Row(
                    []ssr.NodeModel{
                        *ssr.Card(
                            fmt.Sprintf("%d", stats.Users),
                            "Active Users",
                            2.0,
                            ssr.Modifier().WithFillMaxWidth().WithPaddingEnd(8),
                        ),
                        *ssr.Card(
                            fmt.Sprintf("%d", stats.Orders),
                            "Orders Today",
                            2.0,
                            ssr.Modifier().WithFillMaxWidth(),
                        ),
                    },
                    ssr.Modifier().WithFillMaxWidth().WithPaddingBottom(16),
                ),
                *ssr.Card(
                    stats.Revenue,
                    "Total Revenue",
                    4.0,
                    ssr.Modifier().WithFillMaxWidth(),
                ),
            },
            ssr.Modifier().WithFillMaxSize().WithPadding(16),
        ),
    )

    respondJSON(w, screen)
}

func respondJSON(w http.ResponseWriter, node *ssr.NodeModel) {
    w.Header().Set("Content-Type", "application/json")
    json.NewEncoder(w).Encode(node)
}
```

### 3. Product List from API

```go
type Product struct {
    ID          string
    Name        string
    Price       string
    ImageURL    string
    Description string
}

func getProducts(w http.ResponseWriter, r *http.Request) {
    // Fetch products from database
    products := fetchProducts() // Returns []Product

    var cards []ssr.NodeModel
    for _, p := range products {
        card := ssr.CardWithChildren(
            []ssr.NodeModel{
                *ssr.Image(p.ImageURL, 0, 0, p.Name, ssr.Modifier().WithFillMaxWidth()),
                *ssr.Column(
                    []ssr.NodeModel{
                        *ssr.Text(p.Name, ssr.TitleStyle(), nil),
                        *ssr.Text(p.Description, ssr.BodyStyle(), ssr.Modifier().WithPaddingTop(4)),
                        *ssr.Row(
                            []ssr.NodeModel{
                                *ssr.Text(
                                    p.Price,
                                    ssr.TextStyle().WithFontSize(18).WithFontWeight("bold").WithColor("#2E7D32"),
                                    nil,
                                ),
                                *ssr.Box(
                                    []ssr.NodeModel{
                                        *ssr.Button("Buy", "buy:"+p.ID, nil),
                                    },
                                    ssr.Modifier().WithFillMaxWidth().WithContentAlignment("centerend"),
                                ),
                            },
                            ssr.Modifier().WithFillMaxWidth().WithPaddingTop(8),
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

    screen := ssr.Scaffold(
        "Products",
        ssr.LazyColumn(cards, ssr.Modifier().WithFillMaxSize().WithPadding(16)),
    )

    respondJSON(w, screen)
}
```

### 4. Data Table Report

```go
type SalesRecord struct {
    Product string
    Region  string
    Amount  string
    Status  string
}

func getSalesReport(w http.ResponseWriter, r *http.Request) {
    records := fetchSalesRecords() // Returns []SalesRecord

    columns := []ssr.TableColumnModel{
        ssr.NewTableColumnWithStyle(
            "Product",
            2.0,
            "start",
            ssr.TextStyle().WithColor("#FFFFFF"),
            nil,
        ),
        ssr.NewTableColumnWithStyle(
            "Region",
            1.5,
            "center",
            ssr.TextStyle().WithColor("#FFFFFF"),
            nil,
        ),
        ssr.NewTableColumnWithStyle(
            "Amount",
            1.0,
            "end",
            ssr.TextStyle().WithColor("#FFFFFF"),
            ssr.TextStyle().WithFontWeight("bold").WithColor("#2E7D32"),
        ),
        ssr.NewTableColumnWithStyle(
            "Status",
            1.0,
            "center",
            ssr.TextStyle().WithColor("#FFFFFF"),
            nil,
        ),
    }

    var tableData [][]ssr.TableCellModel
    for _, record := range records {
        statusColor := "#4CAF50" // Green
        if record.Status == "Pending" {
            statusColor = "#FF9800" // Orange
        }

        row := []ssr.TableCellModel{
            ssr.NewTableCell(record.Product),
            ssr.NewTableCell(record.Region),
            ssr.NewTableCell(record.Amount),
            ssr.NewTableCellWithStyle(
                record.Status,
                statusColor,
                ssr.WhiteTextStyle(),
            ),
        }
        tableData = append(tableData, row)
    }

    screen := ssr.Scaffold(
        "Sales Report",
        ssr.Column(
            []ssr.NodeModel{
                *ssr.Text("Q4 Performance", ssr.HeadingStyle(), ssr.Modifier().WithPaddingBottom(16)),
                *ssr.Table(columns, tableData, true, "#1976D2", ssr.Modifier().WithFillMaxWidth()),
            },
            ssr.Modifier().WithFillMaxSize().WithPadding(16),
        ),
    )

    respondJSON(w, screen)
}
```

## Common Patterns

### Pattern 1: Reusable UI Components

```go
// Create reusable component builders

func StatCard(title, value string) *ssr.NodeModel {
    return ssr.Card(
        value,
        title,
        2.0,
        ssr.Modifier().WithFillMaxWidth(),
    )
}

func StatsRow(users, orders, revenue int) *ssr.NodeModel {
    return ssr.Row(
        []ssr.NodeModel{
            *StatCard("Users", fmt.Sprintf("%d", users)),
            *StatCard("Orders", fmt.Sprintf("%d", orders)),
            *StatCard("Revenue", fmt.Sprintf("$%d", revenue)),
        },
        ssr.Modifier().WithFillMaxWidth(),
    )
}

// Use in your handlers
func dashboard(w http.ResponseWriter, r *http.Request) {
    stats := getStats()

    screen := ssr.Scaffold(
        "Dashboard",
        ssr.Column(
            []ssr.NodeModel{
                *StatsRow(stats.Users, stats.Orders, stats.Revenue),
                // ... more content
            },
            ssr.Modifier().WithPadding(16),
        ),
    )

    respondJSON(w, screen)
}
```

### Pattern 2: Conditional UI

```go
func getUserProfile(w http.ResponseWriter, r *http.Request) {
    user := getCurrentUser(r)

    var children []ssr.NodeModel

    // Always show profile header
    children = append(children,
        *ssr.Text(user.Name, ssr.HeadingStyle(), nil),
    )

    // Conditionally show admin panel
    if user.IsAdmin {
        children = append(children,
            *ssr.Card(
                "Admin Panel",
                "Manage users and settings",
                4.0,
                nil,
            ),
            *ssr.Button("Manage Users", "navigate:admin", nil),
        )
    }

    // Show regular content
    children = append(children,
        *ssr.Button("Edit Profile", "navigate:edit", nil),
    )

    screen := ssr.Scaffold(
        "Profile",
        ssr.Column(children, ssr.Modifier().WithPadding(16)),
    )

    respondJSON(w, screen)
}
```

### Pattern 3: Error Handling

```go
func getContent(w http.ResponseWriter, r *http.Request) {
    data, err := fetchData()
    if err != nil {
        // Show error screen
        errorScreen := ssr.Scaffold(
            "Error",
            ssr.Column(
                []ssr.NodeModel{
                    *ssr.Card(
                        "Oops!",
                        "Something went wrong. Please try again.",
                        4.0,
                        ssr.Modifier().
                            WithFillMaxWidth().
                            WithBackgroundColor("#FFEBEE"),
                    ),
                    *ssr.Button("Retry", "action:retry", ssr.Modifier().WithPaddingTop(16)),
                },
                ssr.Modifier().
                    WithFillMaxSize().
                    WithPadding(16).
                    WithHorizontalAlignment("center"),
            ),
        )
        respondJSON(w, errorScreen)
        return
    }

    // Normal flow
    screen := buildScreen(data)
    respondJSON(w, screen)
}
```

### Pattern 4: Pagination with Tables

```go
func getPaginatedTable(w http.ResponseWriter, r *http.Request) {
    page := getPageNumber(r) // From query param
    pageSize := 20

    records, total := fetchPaginatedRecords(page, pageSize)

    // Build table...
    table := buildTableFromRecords(records)

    // Add pagination controls
    var children []ssr.NodeModel
    children = append(children, *table)

    if page > 1 || (page * pageSize) < total {
        paginationRow := ssr.Row(
            []ssr.NodeModel{},
            ssr.Modifier().WithFillMaxWidth().WithPaddingTop(16),
        )

        if page > 1 {
            paginationRow.Children = append(paginationRow.Children,
                *ssr.Button("Previous", fmt.Sprintf("page:%d", page-1), nil),
            )
        }

        if (page * pageSize) < total {
            paginationRow.Children = append(paginationRow.Children,
                *ssr.Box(
                    []ssr.NodeModel{
                        *ssr.Button("Next", fmt.Sprintf("page:%d", page+1), nil),
                    },
                    ssr.Modifier().WithFillMaxWidth().WithContentAlignment("centerend"),
                ),
            )
        }

        children = append(children, *paginationRow)
    }

    screen := ssr.Scaffold(
        "Data",
        ssr.Column(children, ssr.Modifier().WithPadding(16)),
    )

    respondJSON(w, screen)
}
```

## Testing Your API

Test with curl:
```bash
# Pretty-print JSON
curl http://localhost:8080/api/dashboard | jq

# Save to file
curl http://localhost:8080/api/dashboard > screen.json
```

Test with your Android app:
1. Point your app to your backend URL
2. The JSON will automatically render as native UI

## Performance Tips

1. **Reuse NodeModels**: Create templates and reuse them
2. **Batch database queries**: Fetch all data at once
3. **Cache static screens**: Use in-memory cache for screens that don't change often
4. **Use goroutines**: Build independent components concurrently

```go
func buildComplexScreen() *ssr.NodeModel {
    type result struct {
        component *ssr.NodeModel
        err       error
    }

    headerChan := make(chan result)
    statsChan := make(chan result)
    tableChan := make(chan result)

    // Build components concurrently
    go func() {
        header := buildHeader()
        headerChan <- result{header, nil}
    }()

    go func() {
        stats := buildStatsSection()
        statsChan <- result{stats, nil}
    }()

    go func() {
        table := buildTable()
        tableChan <- result{table, nil}
    }()

    // Collect results
    headerRes := <-headerChan
    statsRes := <-statsChan
    tableRes := <-tableChan

    return ssr.Scaffold(
        "Dashboard",
        ssr.Column(
            []ssr.NodeModel{
                *headerRes.component,
                *statsRes.component,
                *tableRes.component,
            },
            ssr.Modifier().WithPadding(16),
        ),
    )
}
```

## Next Steps

1. Check `examples/http_server.go` for a complete server implementation
2. Review `examples/table_example.go` for advanced table usage
3. See `examples/basic_example.go` for component examples
4. Run tests: `go test -v`
5. Run benchmarks: `go test -bench=.`

## Need Help?

- See full API documentation in `README.md`
- Check the Android library docs for component details
- Review example JSON files in `../src/main/assets/examples/`
