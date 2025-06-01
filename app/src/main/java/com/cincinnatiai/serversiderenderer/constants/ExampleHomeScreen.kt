package com.cincinnatiai.serversiderenderer.constants

val homeScreenJson = """
    {
      "screen": {
        "id": "welcome_screen",
        "title": "Welcome Screen",
        "layout": {
          "type": "column",
          "id": "main_container",
          "modifier": {
            "fillMaxSize": true
          },
          "properties": {
              "backgroundColor": "#FFFFFF"
          },
          "children": [
            {
              "type": "text",
              "id": "welcome_text",
              "properties": {
                "text": "Welcome, Jack",
                "style": "headline2",
                "fontWeight": "bold",
                "color": "#000000"
              },
              "modifier": {
                "fillMaxWidth": true,
                "padding": 16
              }
            },
            {
              "type": "card",
              "id": "progress_card",
              "modifier": {
                "fillMaxWidth": true,
                "padding": 16
              },
              "properties": {
                "elevation": 8, 
                "backgroundColor": "#FFFFFF"
              },
              "children": [
                {
                  "type": "column",
                  "id": "card_content",
                  "modifier": {
                    "fillMaxWidth": true,
                    "padding": 16
                  },
                  "children": [
                    {
                      "type": "text",
                      "id": "progress_text",
                      "properties": {
                        "text": "Daily Progress: 90%",
                        "style": "body1"
                      }
                    },
                    {
                      "type": "spacer",
                      "id": "card_spacer",
                      "properties": {
                        "height": 16
                      }
                    },
                    {
                      "type": "row",
                      "id": "button_row",
                      "modifier": {
                        "fillMaxWidth": true
                      },
                      "children": [
                        {
                          "type": "spacer",
                          "id": "button_left_spacer",
                          "modifier": {
                            "weight": 1.0
                          }
                        },
                        {
                          "type": "button",
                          "id": "continue_button",
                          "properties": {
                            "text": "Continue"
                          },
                          "actions": {
                            "onClick": {
                              "type": "navigate",
                              "destination": "profile"
                            }
                          }
                        }
                      ]
                    }
                  ]
                }
              ]
            },
            {
              "type": "spacer",
              "id": "list_spacer",
              "properties": {
                "height": 16
              }
            },
            {
              "type": "lazy_column",
              "id": "cards_list",
              "modifier": {
                "fillMaxWidth": true,
                "weight": 1.0, 
                "padding": 8
              },
              "dataSource": {
                "type": "static",
                "items": [
                  {"id": "1", "title": "Card 1", "description": "First card content"},
                  {"id": "2", "title": "Card 2", "description": "Second card content"},
                  {"id": "3", "title": "Card 3", "description": "Third card content"},
                  {"id": "4", "title": "Card 4", "description": "Fourth card content"},
                  {"id": "5", "title": "Card 5", "description": "Fifth card content"},
                  {"id": "6", "title": "Card 6", "description": "Sixth card content"},
                  {"id": "7", "title": "Card 7", "description": "Seventh card content"},
                  {"id": "8", "title": "Card 8", "description": "Eighth card content"}
                ]
              },
              "itemTemplate": {
                "type": "card_item",
                "layout": {
                  "type": "card",
                  "id": "list_card",
                  "modifier": {
                    "fillMaxWidth": true,
                    "padding": 8
                  },
                  "properties": {
                    "elevation": 4
                  },
                  "children": [
                    {
                      "type": "column",
                      "id": "list_card_content",
                      "modifier": {
                        "padding": 16
                      },
                      "children": [
                        {
                          "type": "text",
                          "id": "card_title",
                          "properties": {
                            "text": "{{item.title}}",
                            "style": "subtitle1",
                            "fontWeight": "bold"
                          }
                        },
                        {
                          "type": "spacer",
                          "id": "card_title_spacer",
                          "properties": {
                            "height": 8
                          }
                        },
                        {
                          "type": "text",
                          "id": "card_description",
                          "properties": {
                            "text": "{{item.description}}",
                            "style": "body2"
                          }
                        }
                      ]
                    }
                  ]
                },
                "actions": {
                  "onClick": {
                    "type": "navigate",
                    "destination": "card_detail",
                    "params": {
                      "cardId": "{{item.id}}"
                    }
                  }
                }
              }
            }
          ]
        }
      }
    }
""".trimIndent()