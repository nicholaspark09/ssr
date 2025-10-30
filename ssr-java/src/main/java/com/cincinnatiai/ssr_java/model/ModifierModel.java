package com.cincinnatiai.ssr_java.model;

public class ModifierModel {
    private Integer height;
    private Integer width;
    private Float weight;
    private Integer padding;
    private Integer paddingTop;
    private Integer paddingBottom;
    private Integer paddingStart;
    private Integer paddingEnd;
    private Boolean fillMaxSize;
    private Boolean fillMaxWidth;
    private String backgroundColor;
    private String horizontalAlignment;
    private String verticalAlignment;
    private String contentAlignment;
    private Boolean verticalScroll;

    public ModifierModel() {}

    public Integer getHeight() { return height; }
    public void setHeight(Integer height) { this.height = height; }

    public Integer getWidth() { return width; }
    public void setWidth(Integer width) { this.width = width; }

    public Float getWeight() { return weight; }
    public void setWeight(Float weight) { this.weight = weight; }

    public Integer getPadding() { return padding; }
    public void setPadding(Integer padding) { this.padding = padding; }

    public Integer getPaddingTop() { return paddingTop; }
    public void setPaddingTop(Integer paddingTop) { this.paddingTop = paddingTop; }

    public Integer getPaddingBottom() { return paddingBottom; }
    public void setPaddingBottom(Integer paddingBottom) { this.paddingBottom = paddingBottom; }

    public Integer getPaddingStart() { return paddingStart; }
    public void setPaddingStart(Integer paddingStart) { this.paddingStart = paddingStart; }

    public Integer getPaddingEnd() { return paddingEnd; }
    public void setPaddingEnd(Integer paddingEnd) { this.paddingEnd = paddingEnd; }

    public Boolean getFillMaxSize() { return fillMaxSize; }
    public void setFillMaxSize(Boolean fillMaxSize) { this.fillMaxSize = fillMaxSize; }

    public Boolean getFillMaxWidth() { return fillMaxWidth; }
    public void setFillMaxWidth(Boolean fillMaxWidth) { this.fillMaxWidth = fillMaxWidth; }

    public String getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(String backgroundColor) { this.backgroundColor = backgroundColor; }

    public String getHorizontalAlignment() { return horizontalAlignment; }
    public void setHorizontalAlignment(String horizontalAlignment) { this.horizontalAlignment = horizontalAlignment; }

    public String getVerticalAlignment() { return verticalAlignment; }
    public void setVerticalAlignment(String verticalAlignment) { this.verticalAlignment = verticalAlignment; }

    public String getContentAlignment() { return contentAlignment; }
    public void setContentAlignment(String contentAlignment) { this.contentAlignment = contentAlignment; }

    public Boolean getVerticalScroll() { return verticalScroll; }
    public void setVerticalScroll(Boolean verticalScroll) { this.verticalScroll = verticalScroll; }
}
