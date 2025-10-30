package com.cincinnatiai.ssr_java.model;

public class TableCellModel {
    private String text;
    private TextStyleModel textStyle;
    private String backgroundColor;
    private String action;
    private ModifierModel modifier;
    private Boolean showBorder;

    public TableCellModel() {}

    public TableCellModel(String text) {
        this.text = text;
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public TextStyleModel getTextStyle() { return textStyle; }
    public void setTextStyle(TextStyleModel textStyle) { this.textStyle = textStyle; }

    public String getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(String backgroundColor) { this.backgroundColor = backgroundColor; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public ModifierModel getModifier() { return modifier; }
    public void setModifier(ModifierModel modifier) { this.modifier = modifier; }

    public Boolean getShowBorder() { return showBorder; }
    public void setShowBorder(Boolean showBorder) { this.showBorder = showBorder; }
}
