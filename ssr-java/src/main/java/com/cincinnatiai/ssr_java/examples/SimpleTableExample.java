package com.cincinnatiai.ssr_java.examples;

import com.cincinnatiai.ssr_java.SSR;
import com.cincinnatiai.ssr_java.model.NodeModel;

/**
 * Example demonstrating how to create a simple employee directory table.
 * This generates the same JSON as the simple_table.json example.
 */
public class SimpleTableExample {

    public static void main(String[] args) {
        NodeModel ui = createSimpleTable();
        String json = SSR.toJson(ui);
        System.out.println(json);
    }

    public static NodeModel createSimpleTable() {
        return SSR.scaffold()
                .topBar(SSR.topAppBar("Simple Table Example"))
                .content(
                        SSR.column()
                                .modifier(SSR.modifier().padding(16))
                                .addChild(
                                        SSR.text("Employee Directory")
                                                .textStyle(SSR.textStyle().fontSize(24).bold())
                                                .modifier(SSR.modifier().paddingBottom(16))
                                )
                                .addChild(
                                        SSR.table()
                                                .showBorders(true)
                                                .headerBackgroundColor("#6200EE")
                                                .addColumn(
                                                        SSR.column("Name")
                                                                .weight(2.0f)
                                                                .horizontalAlignment("start")
                                                )
                                                .addColumn(
                                                        SSR.column("Role")
                                                                .weight(1.5f)
                                                                .horizontalAlignment("start")
                                                )
                                                .addColumn(
                                                        SSR.column("Status")
                                                                .weight(1.0f)
                                                                .horizontalAlignment("center")
                                                )
                                                .addRow(
                                                        SSR.cell("Alice Johnson").build(),
                                                        SSR.cell("Engineer").build(),
                                                        SSR.cell("Active")
                                                                .backgroundColor("#4CAF50")
                                                                .textStyle(SSR.textStyle()
                                                                        .color("#FFFFFF")
                                                                        .bold())
                                                                .build()
                                                )
                                                .addRow(
                                                        SSR.cell("Bob Smith").build(),
                                                        SSR.cell("Designer").build(),
                                                        SSR.cell("Active")
                                                                .backgroundColor("#4CAF50")
                                                                .textStyle(SSR.textStyle()
                                                                        .color("#FFFFFF")
                                                                        .bold())
                                                                .build()
                                                )
                                                .addRow(
                                                        SSR.cell("Carol Williams").build(),
                                                        SSR.cell("Manager").build(),
                                                        SSR.cell("Away")
                                                                .backgroundColor("#FF9800")
                                                                .textStyle(SSR.textStyle()
                                                                        .color("#FFFFFF")
                                                                        .bold())
                                                                .build()
                                                )
                                                .addRow(
                                                        SSR.cell("David Brown").build(),
                                                        SSR.cell("Engineer").build(),
                                                        SSR.cell("Active")
                                                                .backgroundColor("#4CAF50")
                                                                .textStyle(SSR.textStyle()
                                                                        .color("#FFFFFF")
                                                                        .bold())
                                                                .build()
                                                )
                                )
                )
                .build();
    }
}
