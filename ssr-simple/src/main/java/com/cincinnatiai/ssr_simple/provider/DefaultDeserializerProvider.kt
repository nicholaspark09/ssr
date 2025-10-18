package com.cincinnatiai.ssr_simple.provider

import com.cincinnatiai.ssr_simple.model.NodeModel
import com.google.gson.Gson

class DefaultDeserializerProvider(
    private val gson: Gson = Gson()
) : DeserializerProvider {

    override fun deserializeToNodeModel(json: String): NodeModel {
        return gson.fromJson<NodeModel>(json, NodeModel::class.java)
    }
}