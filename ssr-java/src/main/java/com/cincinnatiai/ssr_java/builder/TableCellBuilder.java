package com.cincinnatiai.ssr_java.builder;

import com.cincinnatiai.ssr_java.model.ModifierModel;
import com.cincinnatiai.ssr_java.model.TableCellModel;
import com.cincinnatiai.ssr_java.model.TextStyleModel;

public class TableCellBuilder {
    private final TableCellModel model = new TableCellModel();

    public TableCellBuilder(String text) {
        model.setText(text);
    }

    public TableCellBuilder textStyle(TextStyleModel textStyle) {
        model.setTextStyle(textStyle);
        return this;
    }

    public TableCellBuilder textStyle(TextStyleBuilder builder) {
        model.setTextStyle(builder.build());
        return this;
    }

    public TableCellBuilder backgroundColor(String backgroundColor) {
        model.setBackgroundColor(backgroundColor);
        return this;
    }

    public TableCellBuilder action(String action) {
        model.setAction(action);
        return this;
    }

    public TableCellBuilder modifier(ModifierModel modifier) {
        model.setModifier(modifier);
        return this;
    }

    public TableCellBuilder modifier(ModifierBuilder builder) {
        model.setModifier(builder.build());
        return this;
    }

    public TableCellBuilder showBorder(boolean showBorder) {
        model.setShowBorder(showBorder);
        return this;
    }

    public TableCellModel build() {
        return model;
    }
}
