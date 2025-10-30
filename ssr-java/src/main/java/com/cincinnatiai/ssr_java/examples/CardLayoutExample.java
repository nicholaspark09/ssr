package com.cincinnatiai.ssr_java.examples;

import com.cincinnatiai.ssr_java.SSR;
import com.cincinnatiai.ssr_java.model.NodeModel;

/**
 * Example demonstrating how to create a card-based layout.
 */
public class CardLayoutExample {

    public static void main(String[] args) {
        NodeModel ui = createCardLayout();
        String json = SSR.toJson(ui);
        System.out.println(json);
    }

    public static NodeModel createCardLayout() {
        return SSR.scaffold()
                .topBar(SSR.topAppBar("Card Layout Example"))
                .content(
                        SSR.column()
                                .modifier(SSR.modifier().padding(16).verticalScroll())
                                .addChild(
                                        SSR.card()
                                                .elevation(4)
                                                .modifier(SSR.modifier()
                                                        .fillMaxWidth()
                                                        .paddingBottom(16))
                                                .addChild(
                                                        SSR.column()
                                                                .modifier(SSR.modifier().padding(16))
                                                                .addChild(
                                                                        SSR.text("Product Title")
                                                                                .textStyle(SSR.textStyle()
                                                                                        .fontSize(20)
                                                                                        .bold())
                                                                )
                                                                .addChild(
                                                                        SSR.text("$99.99")
                                                                                .textStyle(SSR.textStyle()
                                                                                        .fontSize(18)
                                                                                        .color("#4CAF50"))
                                                                                .modifier(SSR.modifier().paddingTop(4))
                                                                )
                                                                .addChild(
                                                                        SSR.text("This is a detailed description of the product.")
                                                                                .modifier(SSR.modifier().paddingTop(8))
                                                                )
                                                                .addChild(
                                                                        SSR.button("Add to Cart")
                                                                                .action("add_to_cart")
                                                                                .modifier(SSR.modifier().paddingTop(16))
                                                                )
                                                )
                                )
                                .addChild(
                                        SSR.card()
                                                .elevation(4)
                                                .modifier(SSR.modifier()
                                                        .fillMaxWidth()
                                                        .paddingBottom(16))
                                                .addChild(
                                                        SSR.column()
                                                                .modifier(SSR.modifier().padding(16))
                                                                .addChild(
                                                                        SSR.image("https://example.com/image.jpg")
                                                                                .imageHeight(200)
                                                                                .modifier(SSR.modifier()
                                                                                        .fillMaxWidth()
                                                                                        .paddingBottom(8))
                                                                )
                                                                .addChild(
                                                                        SSR.text("Image Card")
                                                                                .textStyle(SSR.textStyle()
                                                                                        .fontSize(18)
                                                                                        .bold())
                                                                )
                                                                .addChild(
                                                                        SSR.text("A card with an image")
                                                                                .modifier(SSR.modifier().paddingTop(4))
                                                                )
                                                )
                                )
                )
                .build();
    }
}
