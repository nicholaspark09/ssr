package com.cincinnatiai.ssr_simple.provider

import com.cincinnatiai.ssr_simple.model.NodeModel

interface DeserializerProvider {

    fun deserializeToNodeModel(json: String): NodeModel
}
