package com.cincinnatiai.ssr_java.builder;

import com.cincinnatiai.ssr_java.model.TextStyleModel;

public class TextStyleBuilder {
    private final TextStyleModel model = new TextStyleModel();

    public TextStyleBuilder fontSize(float fontSize) {
        model.setFontSize(fontSize);
        return this;
    }

    public TextStyleBuilder fontWeight(String fontWeight) {
        model.setFontWeight(fontWeight);
        return this;
    }

    public TextStyleBuilder bold() {
        model.setFontWeight("bold");
        return this;
    }

    public TextStyleBuilder textAlign(String textAlign) {
        model.setTextAlign(textAlign);
        return this;
    }

    public TextStyleBuilder color(String color) {
        model.setColor(color);
        return this;
    }

    public TextStyleModel build() {
        return model;
    }
}
