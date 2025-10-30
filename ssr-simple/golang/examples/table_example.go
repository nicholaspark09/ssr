package main

import (
	"fmt"
	"log"

	ssr "path/to/your/module/ssr_simple" // Update this import path
)

func main() {
	// Example 1: Simple Employee Directory Table
	employeeTable := createEmployeeTable()
	json, err := employeeTable.ToJSONIndent()
	if err != nil {
		log.Fatal(err)
	}
	fmt.Println("=== Employee Directory ===")
	fmt.Println(json)
	fmt.Println()

	// Example 2: Sales Report Table with Actions
	salesTable := createSalesReportTable()
	json, err = salesTable.ToJSONIndent()
	if err != nil {
		log.Fatal(err)
	}
	fmt.Println("=== Sales Report ===")
	fmt.Println(json)
	fmt.Println()

	// Example 3: Full Screen with Table
	fullScreen := createFullScreenWithTable()
	json, err = fullScreen.ToJSONIndent()
	if err != nil {
		log.Fatal(err)
	}
	fmt.Println("=== Full Screen Dashboard ===")
	fmt.Println(json)
}

func createEmployeeTable() *ssr.NodeModel {
	// Define columns
	columns := []ssr.TableColumnModel{
		ssr.NewTableColumn("Name", 2.0, "start"),
		ssr.NewTableColumn("Role", 1.5, "start"),
		ssr.NewTableColumn("Status", 1.0, "center"),
	}

	// Define table data
	tableData := [][]ssr.TableCellModel{
		{
			ssr.NewTableCell("Alice Johnson"),
			ssr.NewTableCell("Engineer"),
			ssr.NewTableCellWithStyle(
				"Active",
				"#4CAF50",
				ssr.WhiteTextStyle(),
			),
		},
		{
			ssr.NewTableCell("Bob Smith"),
			ssr.NewTableCell("Designer"),
			ssr.NewTableCellWithStyle(
				"Active",
				"#4CAF50",
				ssr.WhiteTextStyle(),
			),
		},
		{
			ssr.NewTableCell("Carol Williams"),
			ssr.NewTableCell("Manager"),
			ssr.NewTableCellWithStyle(
				"Away",
				"#FF9800",
				ssr.WhiteTextStyle(),
			),
		},
	}

	return ssr.Table(
		columns,
		tableData,
		true, // showBorders
		"#6200EE",
		ssr.Modifier().WithFillMaxWidth(),
	)
}

func createSalesReportTable() *ssr.NodeModel {
	// Define styled columns
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
			"Units Sold",
			1.0,
			"end",
			ssr.TextStyle().WithColor("#FFFFFF").WithFontSize(16),
			ssr.TextStyle().WithFontWeight("bold"),
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

	// Define table data with actions
	tableData := [][]ssr.TableCellModel{
		{
			ssr.NewTableCell("Laptop Pro X1"),
			ssr.NewTableCell("North America"),
			ssr.NewTableCell("1,234"),
			ssr.NewTableCellWithStyle(
				"$2,468,000",
				"#E8F5E9",
				nil,
			),
			ssr.NewTableCellWithAction(
				"View",
				"#2196F3",
				"view_laptop_x1",
				ssr.WhiteTextStyle(),
			),
		},
		{
			ssr.NewTableCell("Smartphone Z5"),
			ssr.NewTableCell("Europe"),
			ssr.NewTableCell("5,678"),
			ssr.NewTableCellWithStyle(
				"$3,406,800",
				"#E8F5E9",
				nil,
			),
			ssr.NewTableCellWithAction(
				"View",
				"#2196F3",
				"view_phone_z5",
				ssr.WhiteTextStyle(),
			),
		},
		{
			ssr.NewTableCell("Tablet Ultra"),
			ssr.NewTableCell("Asia Pacific"),
			ssr.NewTableCell("3,456"),
			ssr.NewTableCellWithStyle(
				"$1,728,000",
				"#E8F5E9",
				nil,
			),
			ssr.NewTableCellWithAction(
				"View",
				"#2196F3",
				"view_tablet_ultra",
				ssr.WhiteTextStyle(),
			),
		},
	}

	return ssr.TableWithRowAction(
		columns,
		tableData,
		true,
		"#1976D2",
		"row_clicked",
		ssr.Modifier().WithFillMaxWidth(),
	)
}

func createFullScreenWithTable() *ssr.NodeModel {
	// Build a complete screen with table
	return ssr.Scaffold(
		"Q4 Sales Report",
		ssr.Column(
			[]ssr.NodeModel{
				*ssr.Text(
					"Quarter 4 Results",
					ssr.TextStyle().
						WithFontSize(28).
						WithFontWeight("bold").
						WithColor("#1976D2"),
					ssr.Modifier().WithPaddingBottom(8),
				),
				*ssr.Text(
					"Click on any row to view details",
					ssr.TextStyle().
						WithFontSize(14).
						WithColor("#666666"),
					ssr.Modifier().WithPaddingBottom(16),
				),
				*createSalesReportTable(),
			},
			ssr.Modifier().WithFillMaxSize().WithPadding(16),
		),
	)
}

// Helper function to create status cells with appropriate colors
func createStatusCell(status string) ssr.TableCellModel {
	var backgroundColor string
	switch status {
	case "Active":
		backgroundColor = "#4CAF50" // Green
	case "Pending":
		backgroundColor = "#FF9800" // Orange
	case "Inactive":
		backgroundColor = "#F44336" // Red
	default:
		backgroundColor = "#9E9E9E" // Gray
	}

	return ssr.NewTableCellWithStyle(
		status,
		backgroundColor,
		ssr.WhiteTextStyle(),
	)
}

// Helper function to create revenue cells with color coding
func createRevenueCell(amount string, isHigh bool) ssr.TableCellModel {
	bgColor := "#FFF9C4" // Light yellow for normal
	if isHigh {
		bgColor = "#E8F5E9" // Light green for high revenue
	}

	return ssr.NewTableCellWithStyle(
		amount,
		bgColor,
		ssr.TextStyle().
			WithFontWeight("bold").
			WithColor("#2E7D32"),
	)
}
