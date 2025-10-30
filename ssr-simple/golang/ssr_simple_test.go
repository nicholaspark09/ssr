package ssr_simple

import (
	"encoding/json"
	"testing"
)

func TestBasicCard(t *testing.T) {
	card := Card(
		"Test Card",
		"Test Description",
		4.0,
		Modifier().WithFillMaxWidth().WithPadding(16),
	)

	jsonStr, err := card.ToJSON()
	if err != nil {
		t.Fatalf("Failed to convert to JSON: %v", err)
	}

	// Verify it's valid JSON
	var result map[string]interface{}
	if err := json.Unmarshal([]byte(jsonStr), &result); err != nil {
		t.Fatalf("Invalid JSON generated: %v", err)
	}

	// Check type
	if result["type"] != "Card" {
		t.Errorf("Expected type 'Card', got '%v'", result["type"])
	}

	// Check title
	if result["title"] != "Test Card" {
		t.Errorf("Expected title 'Test Card', got '%v'", result["title"])
	}
}

func TestScaffold(t *testing.T) {
	screen := Scaffold(
		"Test Screen",
		Column(
			[]NodeModel{
				*Text("Hello", nil, nil),
			},
			nil,
		),
	)

	jsonStr, err := screen.ToJSON()
	if err != nil {
		t.Fatalf("Failed to convert to JSON: %v", err)
	}

	var result map[string]interface{}
	if err := json.Unmarshal([]byte(jsonStr), &result); err != nil {
		t.Fatalf("Invalid JSON generated: %v", err)
	}

	if result["type"] != "Scaffold" {
		t.Errorf("Expected type 'Scaffold', got '%v'", result["type"])
	}

	// Check topBar exists
	if result["topBar"] == nil {
		t.Error("Expected topBar to be present")
	}

	// Check content exists
	if result["content"] == nil {
		t.Error("Expected content to be present")
	}
}

func TestTable(t *testing.T) {
	columns := []TableColumnModel{
		NewTableColumn("Name", 2.0, "start"),
		NewTableColumn("Status", 1.0, "center"),
	}

	tableData := [][]TableCellModel{
		{
			NewTableCell("Alice"),
			NewTableCellWithStyle("Active", "#4CAF50", WhiteTextStyle()),
		},
	}

	table := Table(
		columns,
		tableData,
		true,
		"#1976D2",
		Modifier().WithFillMaxWidth(),
	)

	jsonStr, err := table.ToJSON()
	if err != nil {
		t.Fatalf("Failed to convert to JSON: %v", err)
	}

	var result map[string]interface{}
	if err := json.Unmarshal([]byte(jsonStr), &result); err != nil {
		t.Fatalf("Invalid JSON generated: %v", err)
	}

	if result["type"] != "Table" {
		t.Errorf("Expected type 'Table', got '%v'", result["type"])
	}

	// Check columns
	if result["columns"] == nil {
		t.Error("Expected columns to be present")
	}

	// Check tableData
	if result["tableData"] == nil {
		t.Error("Expected tableData to be present")
	}

	// Check showBorders
	if result["showBorders"] != true {
		t.Error("Expected showBorders to be true")
	}
}

func TestModifierChaining(t *testing.T) {
	modifier := Modifier().
		WithPadding(16).
		WithFillMaxWidth().
		WithBackgroundColor("#E3F2FD").
		WithHorizontalAlignment("center")

	if modifier.Padding == nil || *modifier.Padding != 16 {
		t.Error("Padding not set correctly")
	}

	if modifier.FillMaxWidth == nil || *modifier.FillMaxWidth != true {
		t.Error("FillMaxWidth not set correctly")
	}

	if modifier.BackgroundColor == nil || *modifier.BackgroundColor != "#E3F2FD" {
		t.Error("BackgroundColor not set correctly")
	}

	if modifier.HorizontalAlignment == nil || *modifier.HorizontalAlignment != "center" {
		t.Error("HorizontalAlignment not set correctly")
	}
}

func TestTextStyleChaining(t *testing.T) {
	style := TextStyle().
		WithFontSize(18).
		WithFontWeight("bold").
		WithColor("#212121").
		WithTextAlign("center")

	if style.FontSize == nil || *style.FontSize != 18 {
		t.Error("FontSize not set correctly")
	}

	if style.FontWeight == nil || *style.FontWeight != "bold" {
		t.Error("FontWeight not set correctly")
	}

	if style.Color == nil || *style.Color != "#212121" {
		t.Error("Color not set correctly")
	}

	if style.TextAlign == nil || *style.TextAlign != "center" {
		t.Error("TextAlign not set correctly")
	}
}

func TestPredefinedStyles(t *testing.T) {
	heading := HeadingStyle()
	if heading.FontSize == nil || *heading.FontSize != 24 {
		t.Error("HeadingStyle should have fontSize 24")
	}

	title := TitleStyle()
	if title.FontSize == nil || *title.FontSize != 18 {
		t.Error("TitleStyle should have fontSize 18")
	}

	body := BodyStyle()
	if body.FontSize == nil || *body.FontSize != 14 {
		t.Error("BodyStyle should have fontSize 14")
	}

	white := WhiteTextStyle()
	if white.Color == nil || *white.Color != "#FFFFFF" {
		t.Error("WhiteTextStyle should have color #FFFFFF")
	}
}

func TestComplexStructure(t *testing.T) {
	// Test a complex nested structure
	screen := Scaffold(
		"Dashboard",
		LazyColumn(
			[]NodeModel{
				*Text("Welcome", HeadingStyle(), nil),
				*Row(
					[]NodeModel{
						*Card("42", "Tasks", 2.0, Modifier().WithFillMaxWidth()),
						*Card("18", "Projects", 2.0, Modifier().WithFillMaxWidth()),
					},
					Modifier().WithFillMaxWidth(),
				),
				*Button("View All", "navigate:tasks", nil),
			},
			Modifier().WithFillMaxSize().WithPadding(16),
		),
	)

	jsonStr, err := screen.ToJSONIndent()
	if err != nil {
		t.Fatalf("Failed to convert complex structure to JSON: %v", err)
	}

	// Just verify it's valid JSON and can be unmarshaled
	var result map[string]interface{}
	if err := json.Unmarshal([]byte(jsonStr), &result); err != nil {
		t.Fatalf("Invalid JSON generated: %v", err)
	}

	// Verify structure
	if result["type"] != "Scaffold" {
		t.Error("Root should be Scaffold")
	}

	content, ok := result["content"].(map[string]interface{})
	if !ok {
		t.Error("content should be an object")
	}

	if content["type"] != "LazyColumn" {
		t.Error("content should be LazyColumn")
	}

	children, ok := content["children"].([]interface{})
	if !ok || len(children) != 3 {
		t.Errorf("Expected 3 children in LazyColumn, got %d", len(children))
	}
}

func TestOmitemptyFields(t *testing.T) {
	// Test that nil fields are omitted from JSON
	text := Text("Simple Text", nil, nil)

	jsonStr, err := text.ToJSON()
	if err != nil {
		t.Fatalf("Failed to convert to JSON: %v", err)
	}

	var result map[string]interface{}
	if err := json.Unmarshal([]byte(jsonStr), &result); err != nil {
		t.Fatalf("Invalid JSON generated: %v", err)
	}

	// textStyle should not be present if nil
	if _, exists := result["textStyle"]; exists {
		t.Error("textStyle should be omitted when nil")
	}

	// modifier should not be present if nil
	if _, exists := result["modifier"]; exists {
		t.Error("modifier should be omitted when nil")
	}

	// title should be present
	if _, exists := result["title"]; !exists {
		t.Error("title should be present")
	}
}

func BenchmarkCardCreation(b *testing.B) {
	for i := 0; i < b.N; i++ {
		Card(
			"Benchmark Card",
			"Testing performance",
			4.0,
			Modifier().WithFillMaxWidth().WithPadding(16),
		)
	}
}

func BenchmarkJSONSerialization(b *testing.B) {
	card := Card(
		"Benchmark Card",
		"Testing performance",
		4.0,
		Modifier().WithFillMaxWidth().WithPadding(16),
	)

	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		_, _ = card.ToJSON()
	}
}

func BenchmarkComplexStructure(b *testing.B) {
	for i := 0; i < b.N; i++ {
		Scaffold(
			"Dashboard",
			LazyColumn(
				[]NodeModel{
					*Text("Welcome", HeadingStyle(), nil),
					*Row(
						[]NodeModel{
							*Card("42", "Tasks", 2.0, nil),
							*Card("18", "Projects", 2.0, nil),
						},
						nil,
					),
				},
				Modifier().WithPadding(16),
			),
		)
	}
}
