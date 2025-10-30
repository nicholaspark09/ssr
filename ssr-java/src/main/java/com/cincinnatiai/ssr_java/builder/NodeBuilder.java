package com.cincinnatiai.ssr_java.builder;

import com.cincinnatiai.ssr_java.model.ModifierModel;
import com.cincinnatiai.ssr_java.model.NodeModel;
import com.cincinnatiai.ssr_java.model.TableCellModel;
import com.cincinnatiai.ssr_java.model.TableColumnModel;
import com.cincinnatiai.ssr_java.model.TextStyleModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NodeBuilder {
    private final NodeModel model = new NodeModel();

    public NodeBuilder(String type) {
        model.setType(type);
    }

    public NodeBuilder title(String title) {
        model.setTitle(title);
        return this;
    }

    public NodeBuilder description(String description) {
        model.setDescription(description);
        return this;
    }

    public NodeBuilder label(String label) {
        model.setLabel(label);
        return this;
    }

    public NodeBuilder backgroundColor(String backgroundColor) {
        model.setBackgroundColor(backgroundColor);
        return this;
    }

    public NodeBuilder elevation(float elevation) {
        model.setElevation(elevation);
        return this;
    }

    public NodeBuilder imageUrl(String imageUrl) {
        model.setImageUrl(imageUrl);
        return this;
    }

    public NodeBuilder imageHeight(float imageHeight) {
        model.setImageHeight(imageHeight);
        return this;
    }

    public NodeBuilder imageWidth(float imageWidth) {
        model.setImageWidth(imageWidth);
        return this;
    }

    public NodeBuilder contentDescription(String contentDescription) {
        model.setContentDescription(contentDescription);
        return this;
    }

    public NodeBuilder action(String action) {
        model.setAction(action);
        return this;
    }

    public NodeBuilder modifier(ModifierModel modifier) {
        model.setModifier(modifier);
        return this;
    }

    public NodeBuilder modifier(ModifierBuilder builder) {
        model.setModifier(builder.build());
        return this;
    }

    public NodeBuilder textStyle(TextStyleModel textStyle) {
        model.setTextStyle(textStyle);
        return this;
    }

    public NodeBuilder textStyle(TextStyleBuilder builder) {
        model.setTextStyle(builder.build());
        return this;
    }

    public NodeBuilder topBar(NodeModel topBar) {
        model.setTopBar(topBar);
        return this;
    }

    public NodeBuilder topBar(NodeBuilder builder) {
        model.setTopBar(builder.build());
        return this;
    }

    public NodeBuilder floatingActionButton(NodeModel floatingActionButton) {
        model.setFloatingActionButton(floatingActionButton);
        return this;
    }

    public NodeBuilder floatingActionButton(NodeBuilder builder) {
        model.setFloatingActionButton(builder.build());
        return this;
    }

    public NodeBuilder content(NodeModel content) {
        model.setContent(content);
        return this;
    }

    public NodeBuilder content(NodeBuilder builder) {
        model.setContent(builder.build());
        return this;
    }

    public NodeBuilder children(List<NodeModel> children) {
        model.setChildren(children);
        return this;
    }

    public NodeBuilder children(NodeModel... children) {
        model.setChildren(Arrays.asList(children));
        return this;
    }

    public NodeBuilder addChild(NodeModel child) {
        if (model.getChildren() == null) {
            model.setChildren(new ArrayList<>());
        }
        model.getChildren().add(child);
        return this;
    }

    public NodeBuilder addChild(NodeBuilder builder) {
        return addChild(builder.build());
    }

    public NodeBuilder buttonVariant(String buttonVariant) {
        model.setButtonVariant(buttonVariant);
        return this;
    }

    public NodeBuilder columns(List<TableColumnModel> columns) {
        model.setColumns(columns);
        return this;
    }

    public NodeBuilder columns(TableColumnModel... columns) {
        model.setColumns(Arrays.asList(columns));
        return this;
    }

    public NodeBuilder addColumn(TableColumnModel column) {
        if (model.getColumns() == null) {
            model.setColumns(new ArrayList<>());
        }
        model.getColumns().add(column);
        return this;
    }

    public NodeBuilder addColumn(TableColumnBuilder builder) {
        return addColumn(builder.build());
    }

    public NodeBuilder tableData(List<List<TableCellModel>> tableData) {
        model.setTableData(tableData);
        return this;
    }

    public NodeBuilder addRow(List<TableCellModel> row) {
        if (model.getTableData() == null) {
            model.setTableData(new ArrayList<>());
        }
        model.getTableData().add(row);
        return this;
    }

    public NodeBuilder addRow(TableCellModel... cells) {
        return addRow(Arrays.asList(cells));
    }

    public NodeBuilder showBorders(boolean showBorders) {
        model.setShowBorders(showBorders);
        return this;
    }

    public NodeBuilder headerBackgroundColor(String headerBackgroundColor) {
        model.setHeaderBackgroundColor(headerBackgroundColor);
        return this;
    }

    public NodeBuilder rowAction(String rowAction) {
        model.setRowAction(rowAction);
        return this;
    }

    public NodeBuilder roundedCorners(float roundedCorners) {
        model.setRoundedCorners(roundedCorners);
        return this;
    }

    public NodeBuilder useLazyColumn(boolean useLazyColumn) {
        model.setUseLazyColumn(useLazyColumn);
        return this;
    }

    public NodeModel build() {
        return model;
    }
}
