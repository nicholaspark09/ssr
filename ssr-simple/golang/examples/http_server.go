package main

import (
	"encoding/json"
	"log"
	"net/http"
	"strconv"

	ssr "path/to/your/module/ssr_simple" // Update this import path
)

func main() {
	http.HandleFunc("/api/dashboard", handleDashboard)
	http.HandleFunc("/api/product-list", handleProductList)
	http.HandleFunc("/api/sales-report", handleSalesReport)
	http.HandleFunc("/api/employee-directory", handleEmployeeDirectory)

	log.Println("Server starting on :8080")
	log.Fatal(http.ListenAndServe(":8080", nil))
}

// handleDashboard returns a dashboard UI
func handleDashboard(w http.ResponseWriter, r *http.Request) {
	// Simulate fetching data from database
	stats := getStatistics()

	screen := ssr.Scaffold(
		"Dashboard",
		ssr.LazyColumn(
			[]ssr.NodeModel{
				*ssr.Text(
					"Welcome Back!",
					ssr.HeadingStyle(),
					ssr.Modifier().WithPaddingBottom(16),
				),
				// Stats row
				*ssr.Row(
					[]ssr.NodeModel{
						*ssr.Card(
							strconv.Itoa(stats.TotalTasks),
							"Total Tasks",
							2.0,
							ssr.Modifier().WithFillMaxWidth().WithPaddingEnd(8),
						),
						*ssr.Card(
							strconv.Itoa(stats.ActiveProjects),
							"Active Projects",
							2.0,
							ssr.Modifier().WithFillMaxWidth(),
						),
					},
					ssr.Modifier().WithFillMaxWidth().WithPaddingBottom(16),
				),
				// Action buttons
				*ssr.Button(
					"View All Tasks",
					"navigate:tasks",
					ssr.Modifier().WithFillMaxWidth().WithPaddingBottom(8),
				),
				*ssr.Button(
					"View Reports",
					"navigate:reports",
					ssr.Modifier().WithFillMaxWidth(),
				),
			},
			ssr.Modifier().WithFillMaxSize().WithPadding(16),
		),
	)

	respondWithJSON(w, screen)
}

// handleProductList returns a product catalog UI
func handleProductList(w http.ResponseWriter, r *http.Request) {
	products := getProducts()

	var productCards []ssr.NodeModel
	for _, product := range products {
		card := ssr.CardWithChildren(
			[]ssr.NodeModel{
				*ssr.Image(
					product.ImageURL,
					0, 0, // Natural size
					product.Name,
					ssr.Modifier().WithFillMaxWidth(),
				),
				*ssr.Column(
					[]ssr.NodeModel{
						*ssr.Text(
							product.Name,
							ssr.TitleStyle(),
							nil,
						),
						*ssr.Text(
							product.Description,
							ssr.BodyStyle(),
							ssr.Modifier().WithPaddingTop(4).WithPaddingBottom(8),
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
											"add_to_cart:"+product.ID,
											nil,
										),
									},
									ssr.Modifier().
										WithFillMaxWidth().
										WithContentAlignment("centerend"),
								),
							},
							ssr.Modifier().WithFillMaxWidth().WithVerticalAlignment("center"),
						),
					},
					ssr.Modifier().WithPadding(16),
				),
			},
			4.0,
			ssr.Modifier().WithFillMaxWidth().WithPaddingBottom(12),
		)
		productCards = append(productCards, *card)
	}

	screen := ssr.Scaffold(
		"Products",
		ssr.LazyColumn(
			productCards,
			ssr.Modifier().WithFillMaxSize().WithPadding(16),
		),
	)

	respondWithJSON(w, screen)
}

// handleSalesReport returns a sales report table UI
func handleSalesReport(w http.ResponseWriter, r *http.Request) {
	salesData := getSalesData()

	columns := []ssr.TableColumnModel{
		ssr.NewTableColumnWithStyle(
			"Product",
			2.0,
			"start",
			ssr.TextStyle().WithColor("#FFFFFF").WithFontSize(16),
			nil,
		),
		ssr.NewTableColumnWithStyle(
			"Region",
			1.5,
			"center",
			ssr.TextStyle().WithColor("#FFFFFF").WithFontSize(16),
			nil,
		),
		ssr.NewTableColumnWithStyle(
			"Revenue",
			1.5,
			"end",
			ssr.TextStyle().WithColor("#FFFFFF").WithFontSize(16),
			ssr.TextStyle().WithFontWeight("bold").WithColor("#2E7D32"),
		),
		ssr.NewTableColumnWithStyle(
			"Action",
			1.0,
			"center",
			ssr.TextStyle().WithColor("#FFFFFF").WithFontSize(16),
			nil,
		),
	}

	var tableData [][]ssr.TableCellModel
	for _, sale := range salesData {
		row := []ssr.TableCellModel{
			ssr.NewTableCell(sale.ProductName),
			ssr.NewTableCell(sale.Region),
			ssr.NewTableCellWithStyle(
				sale.Revenue,
				"#E8F5E9",
				nil,
			),
			ssr.NewTableCellWithAction(
				"Details",
				"#2196F3",
				"view_details:"+sale.ID,
				ssr.WhiteTextStyle(),
			),
		}
		tableData = append(tableData, row)
	}

	screen := ssr.Scaffold(
		"Sales Report",
		ssr.Column(
			[]ssr.NodeModel{
				*ssr.Text(
					"Q4 Sales Performance",
					ssr.HeadingStyle(),
					ssr.Modifier().WithPaddingBottom(16),
				),
				*ssr.Table(
					columns,
					tableData,
					true,
					"#1976D2",
					ssr.Modifier().WithFillMaxWidth(),
				),
			},
			ssr.Modifier().WithFillMaxSize().WithPadding(16),
		),
	)

	respondWithJSON(w, screen)
}

// handleEmployeeDirectory returns an employee directory table
func handleEmployeeDirectory(w http.ResponseWriter, r *http.Request) {
	employees := getEmployees()

	columns := []ssr.TableColumnModel{
		ssr.NewTableColumn("Name", 2.0, "start"),
		ssr.NewTableColumn("Department", 1.5, "start"),
		ssr.NewTableColumn("Status", 1.0, "center"),
	}

	var tableData [][]ssr.TableCellModel
	for _, emp := range employees {
		var statusColor string
		switch emp.Status {
		case "Active":
			statusColor = "#4CAF50"
		case "Away":
			statusColor = "#FF9800"
		case "Offline":
			statusColor = "#9E9E9E"
		default:
			statusColor = "#9E9E9E"
		}

		row := []ssr.TableCellModel{
			ssr.NewTableCell(emp.Name),
			ssr.NewTableCell(emp.Department),
			ssr.NewTableCellWithStyle(
				emp.Status,
				statusColor,
				ssr.WhiteTextStyle(),
			),
		}
		tableData = append(tableData, row)
	}

	screen := ssr.Scaffold(
		"Employee Directory",
		ssr.Column(
			[]ssr.NodeModel{
				*ssr.Text(
					"Team Members",
					ssr.HeadingStyle(),
					ssr.Modifier().WithPaddingBottom(16),
				),
				*ssr.TableWithRowAction(
					columns,
					tableData,
					true,
					"#6200EE",
					"view_employee",
					ssr.Modifier().WithFillMaxWidth(),
				),
			},
			ssr.Modifier().WithFillMaxSize().WithPadding(16),
		),
	)

	respondWithJSON(w, screen)
}

// Helper function to send JSON response
func respondWithJSON(w http.ResponseWriter, node *ssr.NodeModel) {
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(node)
}

// Mock data structures and functions

type Statistics struct {
	TotalTasks     int
	ActiveProjects int
}

type Product struct {
	ID          string
	Name        string
	Description string
	Price       string
	ImageURL    string
}

type SalesData struct {
	ID          string
	ProductName string
	Region      string
	Revenue     string
}

type Employee struct {
	Name       string
	Department string
	Status     string
}

func getStatistics() Statistics {
	return Statistics{
		TotalTasks:     42,
		ActiveProjects: 18,
	}
}

func getProducts() []Product {
	return []Product{
		{
			ID:          "laptop_001",
			Name:        "Premium Laptop",
			Description: "High-performance laptop for professionals",
			Price:       "$1,299",
			ImageURL:    "https://picsum.photos/400/250?random=1",
		},
		{
			ID:          "phone_001",
			Name:        "Smartphone Pro",
			Description: "Latest flagship smartphone",
			Price:       "$899",
			ImageURL:    "https://picsum.photos/400/250?random=2",
		},
		{
			ID:          "tablet_001",
			Name:        "Tablet Ultra",
			Description: "Large screen tablet for productivity",
			Price:       "$599",
			ImageURL:    "https://picsum.photos/400/250?random=3",
		},
	}
}

func getSalesData() []SalesData {
	return []SalesData{
		{
			ID:          "sale_001",
			ProductName: "Laptop Pro X1",
			Region:      "North America",
			Revenue:     "$2,468,000",
		},
		{
			ID:          "sale_002",
			ProductName: "Smartphone Z5",
			Region:      "Europe",
			Revenue:     "$3,406,800",
		},
		{
			ID:          "sale_003",
			ProductName: "Tablet Ultra",
			Region:      "Asia Pacific",
			Revenue:     "$1,728,000",
		},
	}
}

func getEmployees() []Employee {
	return []Employee{
		{"Alice Johnson", "Engineering", "Active"},
		{"Bob Smith", "Design", "Active"},
		{"Carol Williams", "Management", "Away"},
		{"David Brown", "Engineering", "Active"},
		{"Eve Davis", "Marketing", "Offline"},
	}
}
