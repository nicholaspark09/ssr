package com.cincinnatiai.serversiderenderer.constants

val chartsScreenJson = """
    {
      "version": "1.0",
      "screen": {
        "id": "charts_demo_scrollable",
        "title": "Charts Demo - Scrollable",
        "layout": {
          "type": "column",
          "modifier": {
            "fillMaxSize": true
          },
          "children": [
            {
              "type": "scrollable_column",
              "modifier": {
                "fillMaxSize": true,
                "padding": 16
              },
              "children": [
                {
                  "type": "card",
                  "modifier": {
                    "fillMaxWidth": true,
                    "padding": 8
                  },
                  "properties": {
                    "elevation": 4
                  },
                  "children": [
                    {
                      "type": "bar_chart",
                      "properties": {
                        "title": "Monthly Sales",
                        "subtitle": "Revenue by month",
                        "showLegend": true,
                        "showGrid": true,
                        "showValues": true,
                        "height": 350,
                        "data": [
                          {"label": "Jan", "value": 45000, "color": "#3B82F6"},
                          {"label": "Feb", "value": 52000, "color": "#10B981"},
                          {"label": "Mar", "value": 48000, "color": "#F59E0B"},
                          {"label": "Apr", "value": 61000, "color": "#8B5CF6"},
                          {"label": "May", "value": 55000, "color": "#EC4899"},
                          {"label": "Jun", "value": 67000, "color": "#EF4444"}
                        ]
                      }
                    }
                  ]
                },
                {
                  "type": "spacer",
                  "properties": {
                    "height": 24
                  }
                },
                {
                  "type": "card",
                  "modifier": {
                    "fillMaxWidth": true,
                    "padding": 8
                  },
                  "properties": {
                    "elevation": 4
                  },
                  "children": [
                    {
                      "type": "line_chart",
                      "properties": {
                        "title": "Performance Trends",
                        "subtitle": "Revenue vs Users over time",
                        "showLegend": true,
                        "showGrid": true,
                        "height": 350,
                        "series": [
                          {
                            "name": "Revenue (K${'$'})",
                            "color": "#3B82F6",
                            "data": [
                              {"label": "Jan", "value": 45},
                              {"label": "Feb", "value": 52},
                              {"label": "Mar", "value": 48},
                              {"label": "Apr", "value": 61},
                              {"label": "May", "value": 55},
                              {"label": "Jun", "value": 67}
                            ]
                          },
                          {
                            "name": "Users (K)",
                            "color": "#10B981",
                            "data": [
                              {"label": "Jan", "value": 12},
                              {"label": "Feb", "value": 15},
                              {"label": "Mar", "value": 13},
                              {"label": "Apr", "value": 18},
                              {"label": "May", "value": 16},
                              {"label": "Jun", "value": 21}
                            ]
                          }
                        ]
                      }
                    }
                  ]
                },
                {
                  "type": "spacer",
                  "properties": {
                    "height": 24
                  }
                },
                {
                  "type": "card",
                  "modifier": {
                    "fillMaxWidth": true,
                    "padding": 8
                  },
                  "properties": {
                    "elevation": 4
                  },
                  "children": [
                    {
                      "type": "pie_chart",
                      "properties": {
                        "title": "Market Share",
                        "subtitle": "By product category",
                        "showLegend": true,
                        "showValues": true,
                        "height": 350,
                        "data": [
                          {"label": "Mobile Apps", "value": 35, "color": "#3B82F6"},
                          {"label": "Web Apps", "value": 28, "color": "#10B981"},
                          {"label": "Desktop", "value": 22, "color": "#F59E0B"},
                          {"label": "IoT", "value": 10, "color": "#8B5CF6"},
                          {"label": "Other", "value": 5, "color": "#EF4444"}
                        ]
                      }
                    }
                  ]
                },
                {
                  "type": "spacer",
                  "properties": {
                    "height": 24
                  }
                },
                {
                  "type": "card",
                  "modifier": {
                    "fillMaxWidth": true,
                    "padding": 8
                  },
                  "properties": {
                    "elevation": 4
                  },
                  "children": [
                    {
                      "type": "radar_chart",
                      "properties": {
                        "title": "Team Skills Assessment",
                        "subtitle": "Current skill levels",
                        "showLabels": true,
                        "height": 350,
                        "data": [
                          {"label": "Frontend", "value": 85},
                          {"label": "Backend", "value": 78},
                          {"label": "Mobile", "value": 65},
                          {"label": "DevOps", "value": 72},
                          {"label": "Design", "value": 58},
                          {"label": "Testing", "value": 82}
                        ]
                      }
                    }
                  ]
                }
              ]
            }
          ]
        }
      },
      "theme": {
        "primaryColor": "#3B82F6",
        "secondaryColor": "#10B981",
        "backgroundColor": "#F8FAFC",
        "textColor": "#1E293B"
      }
    }
""".trimIndent()