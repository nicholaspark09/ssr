package com.cincinnatiai.ssr_java.util;

import com.cincinnatiai.ssr_java.model.NodeModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonSerializer {
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static final Gson compactGson = new GsonBuilder()
            .create();

    /**
     * Serialize a NodeModel to a pretty-printed JSON string
     */
    public static String toJson(NodeModel node) {
        return gson.toJson(node);
    }

    /**
     * Serialize a NodeModel to a compact JSON string
     */
    public static String toJsonCompact(NodeModel node) {
        return compactGson.toJson(node);
    }

    /**
     * Deserialize a JSON string to a NodeModel
     */
    public static NodeModel fromJson(String json) {
        return gson.fromJson(json, NodeModel.class);
    }
}
