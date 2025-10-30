package com.cincinnatiai.ssr_java.builder;

import com.cincinnatiai.ssr_java.model.TableColumnModel;
import com.cincinnatiai.ssr_java.model.TextStyleModel;

public class TableColumnBuilder {
    private final TableColumnModel model = new TableColumnModel();

    public TableColumnBuilder(String header) {
        model.setHeader(header);
    }

    public TableColumnBuilder weight(float weight) {
        model.setWeight(weight);
        return this;
    }

    public TableColumnBuilder width(int width) {
        model.setWidth(width);
        return this;
    }

    public TableColumnBuilder horizontalAlignment(String horizontalAlignment) {
        model.setHorizontalAlignment(horizontalAlignment);
        return this;
    }

    public TableColumnBuilder textStyle(TextStyleModel textStyle) {
        model.setTextStyle(textStyle);
        return this;
    }

    public TableColumnBuilder textStyle(TextStyleBuilder builder) {
        model.setTextStyle(builder.build());
        return this;
    }

    public TableColumnBuilder headerStyle(TextStyleModel headerStyle) {
        model.setHeaderStyle(headerStyle);
        return this;
    }

    public TableColumnBuilder headerStyle(TextStyleBuilder builder) {
        model.setHeaderStyle(builder.build());
        return this;
    }

    public TableColumnModel build() {
        return model;
    }
}
