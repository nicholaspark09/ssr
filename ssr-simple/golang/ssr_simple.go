package ssr_simple

import "encoding/json"

type ModifierModel struct {
	Height             *int     `json:"height,omitempty"`
	Width              *int     `json:"width,omitempty"`
	Weight             *float64 `json:"weight,omitempty"`              // For Row/Column children: proportional size
	Padding            *int     `json:"padding,omitempty"`
	PaddingTop         *int     `json:"paddingTop,omitempty"`
	PaddingBottom      *int     `json:"paddingBottom,omitempty"`
	PaddingStart       *int     `json:"paddingStart,omitempty"`
	PaddingEnd         *int     `json:"paddingEnd,omitempty"`
	FillMaxSize        *bool    `json:"fillMaxSize,omitempty"`
	FillMaxWidth       *bool    `json:"fillMaxWidth,omitempty"`
	BackgroundColor    *string  `json:"backgroundColor,omitempty"`
	HorizontalAlignment *string  `json:"horizontalAlignment,omitempty"` // "start", "center", "end"
	VerticalAlignment  *string  `json:"verticalAlignment,omitempty"`   // "top", "center", "bottom"
	ContentAlignment   *string  `json:"contentAlignment,omitempty"`    // "topstart", "center", "bottomend", etc.
	VerticalScroll     *bool    `json:"verticalScroll,omitempty"`      // Enable vertical scrolling for Column
}

type TextStyleModel struct {
	FontSize   *float64 `json:"fontSize,omitempty"`   // Size in sp
	FontWeight *string  `json:"fontWeight,omitempty"` // "normal", "medium", "bold", "light"
	TextAlign  *string  `json:"textAlign,omitempty"`  // "start", "center", "end"
	Color      *string  `json:"color,omitempty"`      // Hex color
}

type TableColumnModel struct {
	Header              string          `json:"header"`
	Weight              *float64        `json:"weight,omitempty"`              // Column weight for flexible width
	Width               *int            `json:"width,omitempty"`               // Fixed width in dp
	HorizontalAlignment *string         `json:"horizontalAlignment,omitempty"` // "start", "center", "end"
	TextStyle           *TextStyleModel `json:"textStyle,omitempty"`
	HeaderStyle         *TextStyleModel `json:"headerStyle,omitempty"`
}

type TableCellModel struct {
	Text            string          `json:"text"`
	TextStyle       *TextStyleModel `json:"textStyle,omitempty"`
	BackgroundColor *string         `json:"backgroundColor,omitempty"`
	Action          *string         `json:"action,omitempty"`
	Modifier        *ModifierModel  `json:"modifier,omitempty"`
	ShowBorder      *bool           `json:"showBorder,omitempty"` // Override table border setting for this cell
}

type NodeModel struct {
	Type                   string              `json:"type"`
	Title                  *string             `json:"title,omitempty"`
	Description            *string             `json:"description,omitempty"`
	Label                  *string             `json:"label,omitempty"`
	BackgroundColor        *string             `json:"backgroundColor,omitempty"`
	Elevation              *float64            `json:"elevation,omitempty"`
	ImageURL               *string             `json:"imageUrl,omitempty"`
	ImageHeight            *float64            `json:"imageHeight,omitempty"`
	ImageWidth             *float64            `json:"imageWidth,omitempty"`
	ContentDescription     *string             `json:"contentDescription,omitempty"`
	Action                 *string             `json:"action,omitempty"`
	Modifier               *ModifierModel      `json:"modifier,omitempty"`
	TextStyle              *TextStyleModel     `json:"textStyle,omitempty"`
	TopBar                 *NodeModel          `json:"topBar,omitempty"`
	FloatingActionButton   *NodeModel          `json:"floatingActionButton,omitempty"`
	Content                *NodeModel          `json:"content,omitempty"`
	Children               []NodeModel         `json:"children,omitempty"`
	// Button-specific properties
	ButtonVariant          *string                `json:"buttonVariant,omitempty"` // "filled", "outlined", "text" (default: "filled")
	// Table-specific properties
	Columns               []TableColumnModel     `json:"columns,omitempty"`
	TableData             [][]TableCellModel     `json:"tableData,omitempty"`
	ShowBorders           *bool                  `json:"showBorders,omitempty"`
	HeaderBackgroundColor *string                `json:"headerBackgroundColor,omitempty"`
	RowAction             *string                `json:"rowAction,omitempty"`
	RoundedCorners        *float64               `json:"roundedCorners,omitempty"` // Rounded corner radius in dp for tables
	UseLazyColumn         *bool                  `json:"useLazyColumn,omitempty"`  // Use LazyColumn for table rows (default: true). Set to false when table is in a scrollable parent
}

func (n *NodeModel) ToJSON() (string, error) {
	data, err := json.Marshal(n)
	if err != nil {
		return "", err
	}
	return string(data), nil
}

func (n *NodeModel) ToJSONIndent() (string, error) {
	data, err := json.MarshalIndent(n, "", "  ")
	if err != nil {
		return "", err
	}
	return string(data), nil
}


func Int(v int) *int                { return &v }
func Float64(v float64) *float64    { return &v }
func String(v string) *string       { return &v }
func Bool(v bool) *bool             { return &v }

// Builder functions for common components

// Scaffold creates a full-screen layout
func Scaffold(title string, content *NodeModel) *NodeModel {
	return &NodeModel{
		Type: "Scaffold",
		TopBar: &NodeModel{
			Type:  "TopAppBar",
			Title: String(title),
		},
		Content: content,
	}
}

// TopAppBar creates a top app bar
func TopAppBar(title string) *NodeModel {
	return &NodeModel{
		Type:  "TopAppBar",
		Title: String(title),
	}
}

// Column creates a vertical layout
func Column(children []NodeModel, modifier *ModifierModel) *NodeModel {
	return &NodeModel{
		Type:     "Column",
		Children: children,
		Modifier: modifier,
	}
}

// Row creates a horizontal layout
func Row(children []NodeModel, modifier *ModifierModel) *NodeModel {
	return &NodeModel{
		Type:     "Row",
		Children: children,
		Modifier: modifier,
	}
}

// Box creates a stacked layout
func Box(children []NodeModel, modifier *ModifierModel) *NodeModel {
	return &NodeModel{
		Type:     "Box",
		Children: children,
		Modifier: modifier,
	}
}

// LazyColumn creates a scrollable vertical list
func LazyColumn(children []NodeModel, modifier *ModifierModel) *NodeModel {
	return &NodeModel{
		Type:     "LazyColumn",
		Children: children,
		Modifier: modifier,
	}
}

// Card creates a Material Design card
func Card(title, description string, elevation float64, modifier *ModifierModel) *NodeModel {
	return &NodeModel{
		Type:        "Card",
		Title:       String(title),
		Description: String(description),
		Elevation:   Float64(elevation),
		Modifier:    modifier,
	}
}

// CardWithChildren creates a card with custom children
func CardWithChildren(children []NodeModel, elevation float64, modifier *ModifierModel) *NodeModel {
	return &NodeModel{
		Type:      "Card",
		Children:  children,
		Elevation: Float64(elevation),
		Modifier:  modifier,
	}
}

// Text creates a text component
func Text(text string, textStyle *TextStyleModel, modifier *ModifierModel) *NodeModel {
	return &NodeModel{
		Type:      "Text",
		Title:     String(text),
		TextStyle: textStyle,
		Modifier:  modifier,
	}
}

// Button creates an interactive button (filled style by default)
func Button(label, action string, modifier *ModifierModel) *NodeModel {
	return &NodeModel{
		Type:     "Button",
		Label:    String(label),
		Action:   String(action),
		Modifier: modifier,
	}
}

// OutlinedButton creates an outlined button with border
func OutlinedButton(label, action string, modifier *ModifierModel) *NodeModel {
	return &NodeModel{
		Type:          "Button",
		Label:         String(label),
		Action:        String(action),
		Modifier:      modifier,
		ButtonVariant: String("outlined"),
	}
}

// TextButton creates a text-only button without background
func TextButton(label, action string, modifier *ModifierModel) *NodeModel {
	return &NodeModel{
		Type:          "Button",
		Label:         String(label),
		Action:        String(action),
		Modifier:      modifier,
		ButtonVariant: String("text"),
	}
}

// ButtonWithVariant creates a button with specific variant
func ButtonWithVariant(label, action, variant string, modifier *ModifierModel) *NodeModel {
	return &NodeModel{
		Type:          "Button",
		Label:         String(label),
		Action:        String(action),
		Modifier:      modifier,
		ButtonVariant: String(variant),
	}
}

// Spacer creates a flexible spacer for layout
func Spacer(modifier *ModifierModel) *NodeModel {
	return &NodeModel{
		Type:     "Spacer",
		Modifier: modifier,
	}
}

// Image creates an image component
func Image(imageURL string, width, height float64, contentDescription string, modifier *ModifierModel) *NodeModel {
	node := &NodeModel{
		Type:               "Image",
		ImageURL:           String(imageURL),
		ContentDescription: String(contentDescription),
		Modifier:           modifier,
	}
	if width > 0 {
		node.ImageWidth = Float64(width)
	}
	if height > 0 {
		node.ImageHeight = Float64(height)
	}
	return node
}

// Table creates a data table
func Table(
	columns []TableColumnModel,
	tableData [][]TableCellModel,
	showBorders bool,
	headerBackgroundColor string,
	modifier *ModifierModel,
) *NodeModel {
	return &NodeModel{
		Type:                  "Table",
		Columns:               columns,
		TableData:             tableData,
		ShowBorders:           Bool(showBorders),
		HeaderBackgroundColor: String(headerBackgroundColor),
		Modifier:              modifier,
	}
}

// TableWithRowAction creates a table with row click actions
func TableWithRowAction(
	columns []TableColumnModel,
	tableData [][]TableCellModel,
	showBorders bool,
	headerBackgroundColor string,
	rowAction string,
	modifier *ModifierModel,
) *NodeModel {
	table := Table(columns, tableData, showBorders, headerBackgroundColor, modifier)
	table.RowAction = String(rowAction)
	return table
}

// TableAdvanced creates a table with all available options
func TableAdvanced(
	columns []TableColumnModel,
	tableData [][]TableCellModel,
	showBorders bool,
	headerBackgroundColor string,
	rowAction string,
	roundedCorners float64,
	useLazyColumn bool,
	modifier *ModifierModel,
) *NodeModel {
	table := &NodeModel{
		Type:                  "Table",
		Columns:               columns,
		TableData:             tableData,
		ShowBorders:           Bool(showBorders),
		HeaderBackgroundColor: String(headerBackgroundColor),
		Modifier:              modifier,
		RoundedCorners:        Float64(roundedCorners),
		UseLazyColumn:         Bool(useLazyColumn),
	}
	if rowAction != "" {
		table.RowAction = String(rowAction)
	}
	return table
}

// NewTableColumn creates a table column definition
func NewTableColumn(header string, weight float64, alignment string) TableColumnModel {
	return TableColumnModel{
		Header:              header,
		Weight:              Float64(weight),
		HorizontalAlignment: String(alignment),
	}
}

// NewTableColumnWithStyle creates a table column with custom styling
func NewTableColumnWithStyle(
	header string,
	weight float64,
	alignment string,
	headerStyle *TextStyleModel,
	textStyle *TextStyleModel,
) TableColumnModel {
	return TableColumnModel{
		Header:              header,
		Weight:              Float64(weight),
		HorizontalAlignment: String(alignment),
		HeaderStyle:         headerStyle,
		TextStyle:           textStyle,
	}
}

// NewTableCell creates a simple table cell
func NewTableCell(text string) TableCellModel {
	return TableCellModel{
		Text: text,
	}
}

// NewTableCellWithStyle creates a table cell with custom styling
func NewTableCellWithStyle(text, backgroundColor string, textStyle *TextStyleModel) TableCellModel {
	cell := TableCellModel{
		Text:      text,
		TextStyle: textStyle,
	}
	if backgroundColor != "" {
		cell.BackgroundColor = String(backgroundColor)
	}
	return cell
}

// NewTableCellWithAction creates a clickable table cell
func NewTableCellWithAction(text, backgroundColor, action string, textStyle *TextStyleModel) TableCellModel {
	cell := NewTableCellWithStyle(text, backgroundColor, textStyle)
	cell.Action = String(action)
	return cell
}

// NewTableCellWithBorder creates a table cell with custom border setting
func NewTableCellWithBorder(text, backgroundColor, action string, textStyle *TextStyleModel, showBorder bool) TableCellModel {
	cell := TableCellModel{
		Text:       text,
		TextStyle:  textStyle,
		ShowBorder: Bool(showBorder),
	}
	if backgroundColor != "" {
		cell.BackgroundColor = String(backgroundColor)
	}
	if action != "" {
		cell.Action = String(action)
	}
	return cell
}

// Common modifier presets

func Modifier() *ModifierModel {
	return &ModifierModel{}
}

func (m *ModifierModel) WithPadding(padding int) *ModifierModel {
	m.Padding = Int(padding)
	return m
}

func (m *ModifierModel) WithPaddingTop(padding int) *ModifierModel {
	m.PaddingTop = Int(padding)
	return m
}

func (m *ModifierModel) WithPaddingBottom(padding int) *ModifierModel {
	m.PaddingBottom = Int(padding)
	return m
}

func (m *ModifierModel) WithPaddingStart(padding int) *ModifierModel {
	m.PaddingStart = Int(padding)
	return m
}

func (m *ModifierModel) WithPaddingEnd(padding int) *ModifierModel {
	m.PaddingEnd = Int(padding)
	return m
}

func (m *ModifierModel) WithFillMaxSize() *ModifierModel {
	m.FillMaxSize = Bool(true)
	return m
}

func (m *ModifierModel) WithFillMaxWidth() *ModifierModel {
	m.FillMaxWidth = Bool(true)
	return m
}

func (m *ModifierModel) WithBackgroundColor(color string) *ModifierModel {
	m.BackgroundColor = String(color)
	return m
}

func (m *ModifierModel) WithHorizontalAlignment(alignment string) *ModifierModel {
	m.HorizontalAlignment = String(alignment)
	return m
}

func (m *ModifierModel) WithVerticalAlignment(alignment string) *ModifierModel {
	m.VerticalAlignment = String(alignment)
	return m
}

func (m *ModifierModel) WithContentAlignment(alignment string) *ModifierModel {
	m.ContentAlignment = String(alignment)
	return m
}

func (m *ModifierModel) WithHeight(height int) *ModifierModel {
	m.Height = Int(height)
	return m
}

func (m *ModifierModel) WithWidth(width int) *ModifierModel {
	m.Width = Int(width)
	return m
}

func (m *ModifierModel) WithVerticalScroll() *ModifierModel {
	m.VerticalScroll = Bool(true)
	return m
}

func (m *ModifierModel) WithWeight(weight float64) *ModifierModel {
	m.Weight = Float64(weight)
	return m
}

// Common text style presets

func TextStyle() *TextStyleModel {
	return &TextStyleModel{}
}

func (t *TextStyleModel) WithFontSize(size float64) *TextStyleModel {
	t.FontSize = Float64(size)
	return t
}

func (t *TextStyleModel) WithFontWeight(weight string) *TextStyleModel {
	t.FontWeight = String(weight)
	return t
}

func (t *TextStyleModel) WithTextAlign(align string) *TextStyleModel {
	t.TextAlign = String(align)
	return t
}

func (t *TextStyleModel) WithColor(color string) *TextStyleModel {
	t.Color = String(color)
	return t
}

// Predefined text styles

func HeadingStyle() *TextStyleModel {
	return &TextStyleModel{
		FontSize:   Float64(24),
		FontWeight: String("bold"),
		Color:      String("#212121"),
	}
}

func TitleStyle() *TextStyleModel {
	return &TextStyleModel{
		FontSize:   Float64(18),
		FontWeight: String("bold"),
		Color:      String("#212121"),
	}
}

func BodyStyle() *TextStyleModel {
	return &TextStyleModel{
		FontSize: Float64(14),
		Color:    String("#757575"),
	}
}

func WhiteTextStyle() *TextStyleModel {
	return &TextStyleModel{
		Color:      String("#FFFFFF"),
		FontWeight: String("bold"),
	}
}
