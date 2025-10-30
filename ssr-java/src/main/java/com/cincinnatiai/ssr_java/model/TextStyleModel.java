package com.cincinnatiai.ssr_java.model;

public class TextStyleModel {
    private Float fontSize;
    private String fontWeight;
    private String textAlign;
    private String color;

    public TextStyleModel() {}

    public TextStyleModel(Float fontSize, String fontWeight, String textAlign, String color) {
        this.fontSize = fontSize;
        this.fontWeight = fontWeight;
        this.textAlign = textAlign;
        this.color = color;
    }

    public Float getFontSize() { return fontSize; }
    public void setFontSize(Float fontSize) { this.fontSize = fontSize; }

    public String getFontWeight() { return fontWeight; }
    public void setFontWeight(String fontWeight) { this.fontWeight = fontWeight; }

    public String getTextAlign() { return textAlign; }
    public void setTextAlign(String textAlign) { this.textAlign = textAlign; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}
