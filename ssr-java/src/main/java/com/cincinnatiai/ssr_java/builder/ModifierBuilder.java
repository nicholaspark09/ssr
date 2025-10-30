package com.cincinnatiai.ssr_java.builder;

import com.cincinnatiai.ssr_java.model.ModifierModel;

public class ModifierBuilder {
    private final ModifierModel model = new ModifierModel();

    public ModifierBuilder height(int height) {
        model.setHeight(height);
        return this;
    }

    public ModifierBuilder width(int width) {
        model.setWidth(width);
        return this;
    }

    public ModifierBuilder weight(float weight) {
        model.setWeight(weight);
        return this;
    }

    public ModifierBuilder padding(int padding) {
        model.setPadding(padding);
        return this;
    }

    public ModifierBuilder paddingTop(int paddingTop) {
        model.setPaddingTop(paddingTop);
        return this;
    }

    public ModifierBuilder paddingBottom(int paddingBottom) {
        model.setPaddingBottom(paddingBottom);
        return this;
    }

    public ModifierBuilder paddingStart(int paddingStart) {
        model.setPaddingStart(paddingStart);
        return this;
    }

    public ModifierBuilder paddingEnd(int paddingEnd) {
        model.setPaddingEnd(paddingEnd);
        return this;
    }

    public ModifierBuilder fillMaxSize() {
        model.setFillMaxSize(true);
        return this;
    }

    public ModifierBuilder fillMaxWidth() {
        model.setFillMaxWidth(true);
        return this;
    }

    public ModifierBuilder backgroundColor(String backgroundColor) {
        model.setBackgroundColor(backgroundColor);
        return this;
    }

    public ModifierBuilder horizontalAlignment(String horizontalAlignment) {
        model.setHorizontalAlignment(horizontalAlignment);
        return this;
    }

    public ModifierBuilder verticalAlignment(String verticalAlignment) {
        model.setVerticalAlignment(verticalAlignment);
        return this;
    }

    public ModifierBuilder contentAlignment(String contentAlignment) {
        model.setContentAlignment(contentAlignment);
        return this;
    }

    public ModifierBuilder verticalScroll() {
        model.setVerticalScroll(true);
        return this;
    }

    public ModifierModel build() {
        return model;
    }
}
