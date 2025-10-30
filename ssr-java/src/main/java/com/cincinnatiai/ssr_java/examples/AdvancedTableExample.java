package com.cincinnatiai.ssr_java.examples;

import com.cincinnatiai.ssr_java.SSR;
import com.cincinnatiai.ssr_java.model.NodeModel;

/**
 * Example demonstrating how to create an advanced sales dashboard table.
 * This generates the same JSON as the advanced_table.json example.
 */
public class AdvancedTableExample {

    public static void main(String[] args) {
        NodeModel ui = createAdvancedTable();
        String json = SSR.toJson(ui);
        System.out.println(json);
    }

    public static NodeModel createAdvancedTable() {
        return SSR.scaffold()
                .topBar(SSR.topAppBar("Sales Dashboard"))
                .content(
                        SSR.column()
                                .modifier(SSR.modifier().padding(16).fillMaxSize())
                                .addChild(
                                        SSR.text("Q4 Sales Report")
                                                .textStyle(SSR.textStyle()
                                                        .fontSize(28)
                                                        .bold()
                                                        .color("#1976D2"))
                                                .modifier(SSR.modifier().paddingBottom(8))
                                )
                                .addChild(
                                        SSR.text("Click on any row to view details")
                                                .textStyle(SSR.textStyle()
                                                        .fontSize(14)
                                                        .color("#666666"))
                                                .modifier(SSR.modifier().paddingBottom(16))
                                )
                                .addChild(
                                        SSR.table()
                                                .showBorders(true)
                                                .headerBackgroundColor("#1976D2")
                                                .rowAction("row_clicked")
                                                .modifier(SSR.modifier().fillMaxWidth())
                                                .addColumn(
                                                        SSR.column("Product")
                                                                .weight(2.0f)
                                                                .horizontalAlignment("start")
                                                                .headerStyle(SSR.textStyle()
                                                                        .color("#FFFFFF")
                                                                        .fontSize(16))
                                                )
                                                .addColumn(
                                                        SSR.column("Region")
                                                                .weight(1.5f)
                                                                .horizontalAlignment("center")
                                                                .headerStyle(SSR.textStyle()
                                                                        .color("#FFFFFF")
                                                                        .fontSize(16))
                                                )
                                                .addColumn(
                                                        SSR.column("Units Sold")
                                                                .weight(1.0f)
                                                                .horizontalAlignment("end")
                                                                .headerStyle(SSR.textStyle()
                                                                        .color("#FFFFFF")
                                                                        .fontSize(16))
                                                                .textStyle(SSR.textStyle().bold())
                                                )
                                                .addColumn(
                                                        SSR.column("Revenue")
                                                                .weight(1.5f)
                                                                .horizontalAlignment("end")
                                                                .headerStyle(SSR.textStyle()
                                                                        .color("#FFFFFF")
                                                                        .fontSize(16))
                                                                .textStyle(SSR.textStyle()
                                                                        .fontSize(14)
                                                                        .bold()
                                                                        .color("#2E7D32"))
                                                )
                                                .addColumn(
                                                        SSR.column("Action")
                                                                .weight(1.0f)
                                                                .horizontalAlignment("center")
                                                                .headerStyle(SSR.textStyle()
                                                                        .color("#FFFFFF")
                                                                        .fontSize(16))
                                                )
                                                .addRow(
                                                        SSR.cell("Laptop Pro X1").build(),
                                                        SSR.cell("North America").build(),
                                                        SSR.cell("1,234").build(),
                                                        SSR.cell("$2,468,000")
                                                                .backgroundColor("#E8F5E9")
                                                                .build(),
                                                        SSR.cell("View")
                                                                .action("view_laptop_x1")
                                                                .backgroundColor("#2196F3")
                                                                .textStyle(SSR.textStyle()
                                                                        .color("#FFFFFF")
                                                                        .bold())
                                                                .build()
                                                )
                                                .addRow(
                                                        SSR.cell("Smartphone Z5").build(),
                                                        SSR.cell("Europe").build(),
                                                        SSR.cell("5,678").build(),
                                                        SSR.cell("$3,406,800")
                                                                .backgroundColor("#E8F5E9")
                                                                .build(),
                                                        SSR.cell("View")
                                                                .action("view_phone_z5")
                                                                .backgroundColor("#2196F3")
                                                                .textStyle(SSR.textStyle()
                                                                        .color("#FFFFFF")
                                                                        .bold())
                                                                .build()
                                                )
                                                .addRow(
                                                        SSR.cell("Tablet Ultra").build(),
                                                        SSR.cell("Asia Pacific").build(),
                                                        SSR.cell("3,456").build(),
                                                        SSR.cell("$1,728,000")
                                                                .backgroundColor("#E8F5E9")
                                                                .build(),
                                                        SSR.cell("View")
                                                                .action("view_tablet_ultra")
                                                                .backgroundColor("#2196F3")
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
