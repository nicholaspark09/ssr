package com.cincinnatiai.serversiderenderer.constants

val profileScreenJson = """{
  "screen": {
    "id": "profile_screen",
    "title": "Profile",
    "layout": {
      "type": "column",
      "id": "profile_container",
      "modifier": {
        "fillMaxSize": true,
        "padding": 16
      },
      "properties": {
        "backgroundColor": "#FFFFFF"
      },
      "children": [
        {
          "type": "spacer",
          "id": "top_spacer",
          "properties": {
            "height": 32
          }
        },
        {
          "type": "image",
          "id": "profile_image",
          "properties": {
            "url": "https://www.theopen.com/-/media/images/player-profile-cut-outs/2024-champ-new/woods_tiger_08793.png",
            "shape": "circle",
            "size": 120, 
            "alignment": "center"
          },
          "modifier": {
            "fillMaxWidth": true
          }
        },
        {
          "type": "spacer",
          "id": "image_name_spacer",
          "properties": {
            "height": 16
          }
        },
        {
          "type": "text",
          "id": "user_name",
          "properties": {
            "text": "Tiger Woods",
            "style": "headline2",
            "fontWeight": "bold",
            "textAlign": "center"
          },
          "modifier": {
            "fillMaxWidth": true
          }
        },
        {
          "type": "spacer",
          "id": "name_list_spacer",
          "properties": {
            "height": 32
          }
        },
        {
          "type": "lazy_column",
          "id": "profile_details",
          "modifier": {
            "fillMaxWidth": true,
            "weight": 1.0
          },
          "dataSource": {
            "type": "static",
            "items": [
              {"label": "Email", "value": "tiger.woods@golf.com"},
              {"label": "Phone", "value": "+1 (555) 123-4567"},
              {"label": "Location", "value": "Jupiter, Florida"},
              {"label": "Profession", "value": "Professional Golfer"},
              {"label": "Major Championships", "value": "15"},
              {"label": "PGA Tour Wins", "value": "82"},
              {"label": "World Ranking", "value": "#54"},
              {"label": "Turned Pro", "value": "1996"},
              {"label": "Birth Date", "value": "December 30, 1975"},
              {"label": "Height", "value": "6'1\" (185 cm)"}
            ]
          },
          "itemTemplate": {
            "type": "profile_item",
            "layout": {
              "type": "card",
              "id": "profile_item_card",
              "modifier": {
                "fillMaxWidth": true,
                "padding": 4
              },
              "properties": {
                "elevation": 2
              },
              "children": [
                {
                  "type": "row",
                  "id": "profile_item_row",
                  "modifier": {
                    "fillMaxWidth": true,
                    "padding": 16
                  },
                  "children": [
                    {
                      "type": "text",
                      "id": "profile_label",
                      "properties": {
                        "text": "{{item.label}}",
                        "style": "body1",
                        "fontWeight": "bold"
                      },
                      "modifier": {
                        "weight": 1.0
                      }
                    },
                    {
                      "type": "text",
                      "id": "profile_value",
                      "properties": {
                        "text": "{{item.value}}",
                        "style": "body1",
                        "textAlign": "end"
                      },
                      "modifier": {
                        "weight": 1.0
                      }
                    }
                  ]
                }
              ]
            }
          }
        }
      ]
    }
  }
}""".trimIndent()