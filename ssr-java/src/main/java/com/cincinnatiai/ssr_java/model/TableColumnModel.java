package com.cincinnatiai.ssr_java.model;

public class TableColumnModel {
    private String header;
    private Float weight;
    private Integer width;
    private String horizontalAlignment;
    private TextStyleModel textStyle;
    private TextStyleModel headerStyle;

    public TableColumnModel() {}

    public TableColumnModel(String header) {
        this.header = header;
    }

    public String getHeader() { return header; }
    public void setHeader(String header) { this.header = header; }

    public Float getWeight() { return weight; }
    public void setWeight(Float weight) { this.weight = weight; }

    public Integer getWidth() { return width; }
    public void setWidth(Integer width) { this.width = width; }

    public String getHorizontalAlignment() { return horizontalAlignment; }
    public void setHorizontalAlignment(String horizontalAlignment) { this.horizontalAlignment = horizontalAlignment; }

    public TextStyleModel getTextStyle() { return textStyle; }
    public void setTextStyle(TextStyleModel textStyle) { this.textStyle = textStyle; }

    public TextStyleModel getHeaderStyle() { return headerStyle; }
    public void setHeaderStyle(TextStyleModel headerStyle) { this.headerStyle = headerStyle; }
}
