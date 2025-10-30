package com.cincinnatiai.ssr_java.model;

import java.util.List;

public class NodeModel {
    private String type;
    private String title;
    private String description;
    private String label;
    private String backgroundColor;
    private Float elevation;
    private String imageUrl;
    private Float imageHeight;
    private Float imageWidth;
    private String contentDescription;
    private String action;
    private ModifierModel modifier;
    private TextStyleModel textStyle;
    private NodeModel topBar;
    private NodeModel floatingActionButton;
    private NodeModel content;
    private List<NodeModel> children;
    private String buttonVariant;
    private List<TableColumnModel> columns;
    private List<List<TableCellModel>> tableData;
    private Boolean showBorders;
    private String headerBackgroundColor;
    private String rowAction;
    private Float roundedCorners;
    private Boolean useLazyColumn;

    public NodeModel() {}

    public NodeModel(String type) {
        this.type = type;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(String backgroundColor) { this.backgroundColor = backgroundColor; }

    public Float getElevation() { return elevation; }
    public void setElevation(Float elevation) { this.elevation = elevation; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Float getImageHeight() { return imageHeight; }
    public void setImageHeight(Float imageHeight) { this.imageHeight = imageHeight; }

    public Float getImageWidth() { return imageWidth; }
    public void setImageWidth(Float imageWidth) { this.imageWidth = imageWidth; }

    public String getContentDescription() { return contentDescription; }
    public void setContentDescription(String contentDescription) { this.contentDescription = contentDescription; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public ModifierModel getModifier() { return modifier; }
    public void setModifier(ModifierModel modifier) { this.modifier = modifier; }

    public TextStyleModel getTextStyle() { return textStyle; }
    public void setTextStyle(TextStyleModel textStyle) { this.textStyle = textStyle; }

    public NodeModel getTopBar() { return topBar; }
    public void setTopBar(NodeModel topBar) { this.topBar = topBar; }

    public NodeModel getFloatingActionButton() { return floatingActionButton; }
    public void setFloatingActionButton(NodeModel floatingActionButton) { this.floatingActionButton = floatingActionButton; }

    public NodeModel getContent() { return content; }
    public void setContent(NodeModel content) { this.content = content; }

    public List<NodeModel> getChildren() { return children; }
    public void setChildren(List<NodeModel> children) { this.children = children; }

    public String getButtonVariant() { return buttonVariant; }
    public void setButtonVariant(String buttonVariant) { this.buttonVariant = buttonVariant; }

    public List<TableColumnModel> getColumns() { return columns; }
    public void setColumns(List<TableColumnModel> columns) { this.columns = columns; }

    public List<List<TableCellModel>> getTableData() { return tableData; }
    public void setTableData(List<List<TableCellModel>> tableData) { this.tableData = tableData; }

    public Boolean getShowBorders() { return showBorders; }
    public void setShowBorders(Boolean showBorders) { this.showBorders = showBorders; }

    public String getHeaderBackgroundColor() { return headerBackgroundColor; }
    public void setHeaderBackgroundColor(String headerBackgroundColor) { this.headerBackgroundColor = headerBackgroundColor; }

    public String getRowAction() { return rowAction; }
    public void setRowAction(String rowAction) { this.rowAction = rowAction; }

    public Float getRoundedCorners() { return roundedCorners; }
    public void setRoundedCorners(Float roundedCorners) { this.roundedCorners = roundedCorners; }

    public Boolean getUseLazyColumn() { return useLazyColumn; }
    public void setUseLazyColumn(Boolean useLazyColumn) { this.useLazyColumn = useLazyColumn; }
}
