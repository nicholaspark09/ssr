package com.cincinnatiai.serversiderenderer.constants

val dashboardJson = """
    {
      "version": "1.0",
      "screen": {
        "id": "baby_progress_dashboard",
        "title": "Baby Progress Dashboard",
        "layout": {
          "type": "scrollable_column",
          "modifier": {
            "fillMaxSize": true,
            "paddingStart": 8,
            "paddingEnd": 8,
            "paddingTop": 16,
            "paddingBottom": 16
          },
          "properties": {
            "backgroundColor": "#F8FAFC"
          },
          "children": [
            {
              "type": "card",
              "modifier": {
                "fillMaxWidth": true,
                "paddingStart": 16,
                "paddingTop": 16,
                "paddingEnd": 16
              },
              "properties": {
                "elevation": 4,
                "backgroundColor": "#FFFFFF"
              },
              "children": [
                {
                  "type": "row",
                  "modifier": {
                    "padding": 8
                  },
                  "properties": {
                    "horizontalArrangement": "spaceBetween"
                  },
                  "children": [
                    {
                      "type": "column",
                      "children": [
                        {
                          "type": "text",
                          "properties": {
                            "text": "Emma's Progress",
                            "style": "headline1",
                            "color": "#000000",
                            "fontWeight": "bold",
                            "fontSize": 22
                          }
                        },
                        {
                          "type": "text",
                          "properties": {
                            "text": "4 months, 16 weeks, 2 days old",
                            "style": "body2",
                            "color": "#000000",
                            "fontSize": 12
                          }
                        }
                      ]
                    },
                    {
                      "type": "column",
                      "modifier": {
                        "paddingStart": 8,
                        "paddingTop": 2
                      },
                      "children": [
                        {
                          "type": "text",
                          "properties": {
                            "text": "Week 16",
                            "style": "headline2",
                            "color": "#8B5CF6",
                            "fontWeight": "bold",
                            "fontSize": 20
                          }
                        },
                        {
                          "type": "text",
                          "properties": {
                            "text": "4-month milestone period",
                            "style": "body2",
                            "fontSize": 10
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
              "properties": {
                "height": 8
              }
            },
            {
              "type": "card",
              "modifier": {
                "fillMaxSize": true,
                "gradient": {
                  "type": "linear",
                  "colors": ["#8B5CF6", "#EC4899"],
                  "angle": 0
                }
              },
              "children": [
                {
                  "type": "text",
                  "modifier": {
                    "paddingTop": 8,
                    "paddingStart": 16,
                    "paddingEnd": 16
                  },
                  "properties": {
                    "text": "This Week's Focus",
                    "style": "headline2", 
                    "color": "#FFFFFF",
                    "fontWeight": "bold",
                    "fontSize": 20
                  }
                },
                {
                  "type": "card",
                  "modifier": {
                    "fillMaxWidth": true,
                    "paddingStart": 16,
                    "paddingEnd": 16,
                    "paddingTop": 8,
                    "paddingBottom": 16
                   },
                  "properties": {
                    "backgroundColor": "#20FFFFFF",
                    "elevation": 0
                   },
                  "children": [
                    {
                      "type": "text",
                      "modifier": {
                        "padding": 8
                        },
                      "properties": {
                        "text": "Developing Reaching & Grasping",
                        "style": "headline2",
                        "color": "#FFFFFF",
                        "fontWeight": "bold",
                        "fontSize": 18
                      }
                    },
                    {
                      "type": "text",
                      "modifier": {
                        "paddingStart": 8,
                        "paddingEnd": 8,
                        "paddingTop": 4
                       },
                      "properties": {
                        "text": "Your baby is learning to coordinate their movements to reach for and grasp objects.",
                        "style": "body1",
                        "color": "#FFFFFF"
                      }
                    },
                    {
                      "type": "spacer",
                      "properties": {
                        "height": 8
                      }
                    },
                    {
                      "type": "text",
                      "modifier": {
                        "padding": 8
                       },
                      "properties": {
                        "text": "• Place colorful toys within reach during tummy time\n• Use rattles and soft textured toys\n• Encourage reaching across their body",
                        "style": "body2",
                        "color": "#FFFFFF"
                      }
                    }
                  ]
                }
              ]
            },
            {
              "type": "spacer",
              "properties": {
                "height": 16
              }
            },
            {
              "type": "card",
              "modifier": {
                "fillMaxWidth": true,
                "padding": 16
              },
              "properties": {
                "elevation": 4,
                "backgroundColor": "#FFFFFF"
              },
              "children": [
                {
                  "type": "text",
                  "properties": {
                    "text": "Development Areas",
                    "style": "headline2"
                  }
                },
                {
                  "type": "spacer",
                  "properties": {
                    "height": 16
                  }
                },
                {
                  "type": "column",
                  "modifier": {
                    "fillMaxWidth": true
                  },
                  "children": [
                    {
                      "type": "card",
                      "modifier": {
                        "fillMaxWidth": true,
                        "padding": 16
                      },
                      "properties": {
                        "elevation": 2,
                        "backgroundColor": "#F8FAFC"
                      },
                      "children": [
                        {
                          "type": "row",
                          "children": [
                            {
                              "type": "text",
                              "properties": {
                                "text": "Physical",
                                "style": "headline2"
                              }
                            }
                          ]
                        },
                        {
                          "type": "spacer",
                          "properties": {
                            "height": 8
                          }
                        },
                        {
                          "type": "bar_chart",
                          "properties": {
                            "showLegend": false,
                            "showGrid": false,
                            "showValues": true,
                            "height": 80,
                            "data": [
                              {
                                "label": "Progress",
                                "value": 75,
                                "color": "#3B82F6"
                              }
                            ]
                          }
                        },
                        {
                          "type": "spacer",
                          "properties": {
                            "height": 8
                          }
                        },
                        {
                          "type": "text",
                          "properties": {
                            "text": "Better head control, reaching for toys",
                            "style": "body2"
                          }
                        }
                      ]
                    },
                    {
                      "type": "spacer",
                      "properties": {
                        "height": 12
                      }
                    },
                    {
                      "type": "card",
                      "modifier": {
                        "fillMaxWidth": true,
                        "padding": 16
                      },
                      "properties": {
                        "elevation": 2,
                        "backgroundColor": "#F8FAFC"
                      },
                      "children": [
                        {
                          "type": "row",
                          "children": [
                            {
                              "type": "text",
                              "properties": {
                                "text": "Cognitive",
                                "style": "headline2"
                              }
                            }
                          ]
                        },
                        {
                          "type": "spacer",
                          "properties": {
                            "height": 8
                          }
                        },
                        {
                          "type": "bar_chart",
                          "properties": {
                            "showLegend": false,
                            "showGrid": false,
                            "showValues": true,
                            "height": 80,
                            "data": [
                              {
                                "label": "Progress",
                                "value": 60,
                                "color": "#8B5CF6"
                              }
                            ]
                          }
                        },
                        {
                          "type": "spacer",
                          "properties": {
                            "height": 8
                          }
                        },
                        {
                          "type": "text",
                          "properties": {
                            "text": "Recognizes familiar faces, follows objects",
                            "style": "body2"
                          }
                        }
                      ]
                    },
                    {
                      "type": "spacer",
                      "properties": {
                        "height": 12
                      }
                    },
                    {
                      "type": "card",
                      "modifier": {
                        "fillMaxWidth": true,
                        "padding": 16
                      },
                      "properties": {
                        "elevation": 2,
                        "backgroundColor": "#F8FAFC"
                      },
                      "children": [
                        {
                          "type": "row",
                          "children": [
                            {
                              "type": "text",
                              "properties": {
                                "text": "Social",
                                "style": "headline2"
                              }
                            }
                          ]
                        },
                        {
                          "type": "spacer",
                          "properties": {
                            "height": 8
                          }
                        },
                        {
                          "type": "bar_chart",
                          "properties": {
                            "showLegend": false,
                            "showGrid": false,
                            "showValues": true,
                            "height": 80,
                            "data": [
                              {
                                "label": "Progress",
                                "value": 80,
                                "color": "#10B981"
                              }
                            ]
                          }
                        },
                        {
                          "type": "spacer",
                          "properties": {
                            "height": 8
                          }
                        },
                        {
                          "type": "text",
                          "properties": {
                            "text": "Smiles responsively, enjoys interaction",
                            "style": "body2"
                          }
                        }
                      ]
                    },
                    {
                      "type": "spacer",
                      "properties": {
                        "height": 12
                      }
                    },
                    {
                      "type": "card",
                      "modifier": {
                        "fillMaxWidth": true,
                        "padding": 16
                      },
                      "properties": {
                        "elevation": 2,
                        "backgroundColor": "#F8FAFC"
                      },
                      "children": [
                        {
                          "type": "row",
                          "children": [
                            {
                              "type": "text",
                              "properties": {
                                "text": "Language",
                                "style": "headline2"
                              }
                            }
                          ]
                        },
                        {
                          "type": "spacer",
                          "properties": {
                            "height": 8
                          }
                        },
                        {
                          "type": "bar_chart",
                          "properties": {
                            "showLegend": false,
                            "showGrid": false,
                            "showValues": true,
                            "height": 80,
                            "data": [
                              {
                                "label": "Progress",
                                "value": 45,
                                "color": "#EC4899"
                              }
                            ]
                          }
                        },
                        {
                          "type": "spacer",
                          "properties": {
                            "height": 8
                          }
                        },
                        {
                          "type": "text",
                          "properties": {
                            "text": "Coos and gurgles, responds to voice",
                            "style": "body2"
                          }
                        }
                      ]
                    }
                  ]
                }
              ]
            }
          ]
        }
      },
      "theme": {
        "primaryColor": "#8B5CF6",
        "secondaryColor": "#EC4899",
        "backgroundColor": "#F8FAFC",
        "textColor": "#1F2937"
      }
    }
""".trimIndent()