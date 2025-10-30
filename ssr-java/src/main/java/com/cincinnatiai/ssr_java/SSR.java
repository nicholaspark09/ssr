package com.cincinnatiai.ssr_java;

import com.cincinnatiai.ssr_java.builder.*;
import com.cincinnatiai.ssr_java.model.*;
import com.cincinnatiai.ssr_java.util.JsonSerializer;

/**
 * Main entry point for the SSR Java library.
 * Provides convenient static methods for creating UI components.
 */
public class SSR {

    // Builder factory methods
    public static NodeBuilder scaffold() {
        return new NodeBuilder("Scaffold");
    }

    public static NodeBuilder topAppBar(String title) {
        return new NodeBuilder("TopAppBar").title(title);
    }

    public static NodeBuilder column() {
        return new NodeBuilder("Column");
    }

    public static NodeBuilder row() {
        return new NodeBuilder("Row");
    }

    public static NodeBuilder box() {
        return new NodeBuilder("Box");
    }

    public static NodeBuilder text(String title) {
        return new NodeBuilder("Text").title(title);
    }

    public static NodeBuilder button(String label) {
        return new NodeBuilder("Button").label(label);
    }

    public static NodeBuilder card() {
        return new NodeBuilder("Card");
    }

    public static NodeBuilder image(String imageUrl) {
        return new NodeBuilder("Image").imageUrl(imageUrl);
    }

    public static NodeBuilder table() {
        return new NodeBuilder("Table");
    }

    public static NodeBuilder spacer(int height) {
        return new NodeBuilder("Spacer").modifier(new ModifierBuilder().height(height).build());
    }

    public static NodeBuilder divider() {
        return new NodeBuilder("Divider");
    }

    // Helper methods for common components
    public static TableColumnBuilder column(String header) {
        return new TableColumnBuilder(header);
    }

    public static TableCellBuilder cell(String text) {
        return new TableCellBuilder(text);
    }

    public static ModifierBuilder modifier() {
        return new ModifierBuilder();
    }

    public static TextStyleBuilder textStyle() {
        return new TextStyleBuilder();
    }

    // JSON serialization methods
    public static String toJson(NodeModel node) {
        return JsonSerializer.toJson(node);
    }

    public static String toJsonCompact(NodeModel node) {
        return JsonSerializer.toJsonCompact(node);
    }

    public static NodeModel fromJson(String json) {
        return JsonSerializer.fromJson(json);
    }
}
