package com.cincinnatiai.serversiderenderer.constants

val variousCards = """{
  "version": "1.0",
  "screen": {
    "id": "cards_demo_screen",
    "title": "Cards Demo Screen",
    "layout": {
      "type": "column",
      "modifier": {
        "fillMaxSize": true,
        "padding": 16
      },
      "children": [
        {
          "type": "text",
          "properties": {
            "text": "Card Examples",
            "style": "headline1"
          },
          "modifier": {
            "padding": 16
          }
        },
        {
          "type": "spacer",
          "properties": {
            "height": 16
          }
        },
        {
          "type": "text",
          "properties": {
            "text": "Simple Cards",
            "style": "headline2"
          },
          "modifier": {
            "padding": 8
          }
        },
        {
          "type": "card",
          "id": "simple_card_1",
          "modifier": {
            "fillMaxWidth": true,
            "padding": 8
          },
          "children": [
            {
              "type": "column",
              "modifier": {
                "padding": 16
              },
              "children": [
                {
                  "type": "text",
                  "properties": {
                    "text": "Simple Card Title",
                    "style": "headline2"
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
                    "text": "This is a simple card with some text content. Cards are great for grouping related information.",
                    "style": "body1"
                  }
                }
              ]
            }
          ]
        },
        {
          "type": "card",
          "id": "profile_card",
          "modifier": {
            "fillMaxWidth": true,
            "padding": 8
          },
          "children": [
            {
              "type": "row",
              "modifier": {
                "padding": 16
              },
              "children": [
                {
                  "type": "image",
                  "properties": {
                    "url": "https://picsum.photos/100/100?random=10",
                    "contentDescription": "Profile picture"
                  },
                  "modifier": {
                    "width": 60,
                    "height": 60
                  }
                },
                {
                  "type": "spacer",
                  "properties": {
                    "width": 16
                  }
                },
                {
                  "type": "column",
                  "modifier": {
                    "weight": 1
                  },
                  "children": [
                    {
                      "type": "text",
                      "properties": {
                        "text": "John Doe",
                        "style": "headline2"
                      }
                    },
                    {
                      "type": "text",
                      "properties": {
                        "text": "Software Engineer",
                        "style": "subtitle1"
                      }
                    },
                    {
                      "type": "text",
                      "properties": {
                        "text": "Building amazing mobile applications with passion and precision.",
                        "style": "body2"
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
            "height": 16
          }
        },
        {
          "type": "text",
          "properties": {
            "text": "Card Grid",
            "style": "headline2"
          },
          "modifier": {
            "padding": 8
          }
        },
        {
          "type": "row",
          "modifier": {
            "fillMaxWidth": true
          },
          "children": [
            {
              "type": "card",
              "id": "metric_card_1",
              "modifier": {
                "weight": 1,
                "padding": 4
              },
              "children": [
                {
                  "type": "column",
                  "modifier": {
                    "padding": 16
                  },
                  "children": [
                    {
                      "type": "text",
                      "properties": {
                        "text": "Sales",
                        "style": "body2"
                      }
                    },
                    {
                      "type": "text",
                      "properties": {
                        "text": "${'$'}24,560",
                        "style": "headline1"
                      }
                    },
                    {
                      "type": "text",
                      "properties": {
                        "text": "+12%",
                        "style": "body2"
                      }
                    }
                  ]
                }
              ]
            },
            {
              "type": "card",
              "id": "metric_card_2",
              "modifier": {
                "weight": 1,
                "padding": 4
              },
              "children": [
                {
                  "type": "column",
                  "modifier": {
                    "padding": 16
                  },
                  "children": [
                    {
                      "type": "text",
                      "properties": {
                        "text": "Users",
                        "style": "body2"
                      }
                    },
                    {
                      "type": "text",
                      "properties": {
                        "text": "1,247",
                        "style": "headline1"
                      }
                    },
                    {
                      "type": "text",
                      "properties": {
                        "text": "+8%",
                        "style": "body2"
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
            "height": 16
          }
        },
        {
          "type": "text",
          "properties": {
            "text": "Action Cards",
            "style": "headline2"
          },
          "modifier": {
            "padding": 8
          }
        },
        {
          "type": "card",
          "id": "action_card",
          "modifier": {
            "fillMaxWidth": true,
            "padding": 8
          },
          "children": [
            {
              "type": "column",
              "modifier": {
                "padding": 16
              },
              "children": [
                {
                  "type": "text",
                  "properties": {
                    "text": "Get Started",
                    "style": "headline2"
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
                    "text": "Ready to begin your journey? Click the button below to start exploring our features.",
                    "style": "body1"
                  }
                },
                {
                  "type": "spacer",
                  "properties": {
                    "height": 16
                  }
                },
                {
                  "type": "row",
                  "children": [
                    {
                      "type": "button",
                      "properties": {
                        "text": "Get Started",
                        "enabled": true
                      },
                      "modifier": {
                        "weight": 1
                      },
                      "actions": {
                        "onClick": {
                          "type": "navigation",
                          "destination": "onboarding",
                          "params": {
                            "source": "action_card"
                          }
                        }
                      }
                    },
                    {
                      "type": "spacer",
                      "properties": {
                        "width": 8
                      }
                    },
                    {
                      "type": "button",
                      "properties": {
                        "text": "Learn More",
                        "enabled": true
                      },
                      "modifier": {
                        "weight": 1
                      },
                      "actions": {
                        "onClick": {
                          "type": "navigation",
                          "destination": "learn_more",
                          "params": {
                            "source": "action_card"
                          }
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
          "properties": {
            "height": 16
          }
        },
        {
          "type": "text",
          "properties": {
            "text": "Card List",
            "style": "headline2"
          },
          "modifier": {
            "padding": 8
          }
        },
        {
          "type": "lazy_column",
          "modifier": {
            "fillMaxWidth": true,
            "weight": 1
          },
          "dataSource": {
            "type": "static",
            "items": [
              {
                "id": "news_1",
                "title": "New Feature Release",
                "subtitle": "We've added dark mode support",
                "description": "Experience our app in a whole new way with our beautiful dark theme. Perfect for late-night browsing.",
                "imageUrl": "https://picsum.photos/200/200?random=20",
                "category": "Product",
                "timestamp": "2 hours ago"
              },
              {
                "id": "news_2",
                "title": "Performance Improvements",
                "subtitle": "50% faster loading times",
                "description": "We've optimized our backend infrastructure to deliver lightning-fast performance across all devices.",
                "imageUrl": "https://picsum.photos/200/200?random=21",
                "category": "Engineering",
                "timestamp": "1 day ago"
              },
              {
                "id": "news_3",
                "title": "Community Milestone",
                "subtitle": "100K active users reached!",
                "description": "Thank you to our amazing community for helping us reach this incredible milestone. Here's to the next 100K!",
                "imageUrl": "https://picsum.photos/200/200?random=22",
                "category": "Community",
                "timestamp": "3 days ago"
              },
              {
                "id": "news_4",
                "title": "Security Update",
                "subtitle": "Enhanced data protection",
                "description": "We've implemented additional security measures to keep your data safe and secure at all times.",
                "imageUrl": "https://picsum.photos/200/200?random=23",
                "category": "Security",
                "timestamp": "1 week ago"
              }
            ]
          },
          "itemTemplate": {
            "type": "card",
            "layout": {
              "type": "column",
              "modifier": {
                "fillMaxWidth": true,
                "padding": 16
              },
              "children": [
                {
                  "type": "row",
                  "children": [
                    {
                      "type": "image",
                      "properties": {
                        "url": "{{item.imageUrl}}",
                        "contentDescription": "{{item.title}}"
                      },
                      "modifier": {
                        "width": 80,
                        "height": 80
                      }
                    },
                    {
                      "type": "spacer",
                      "properties": {
                        "width": 16
                      }
                    },
                    {
                      "type": "column",
                      "modifier": {
                        "weight": 1
                      },
                      "children": [
                        {
                          "type": "text",
                          "properties": {
                            "text": "{{item.title}}",
                            "style": "headline2"
                          }
                        },
                        {
                          "type": "text",
                          "properties": {
                            "text": "{{item.subtitle}}",
                            "style": "subtitle1"
                          }
                        },
                        {
                          "type": "spacer",
                          "properties": {
                            "height": 4
                          }
                        },
                        {
                          "type": "row",
                          "children": [
                            {
                              "type": "text",
                              "properties": {
                                "text": "{{item.category}}",
                                "style": "body2"
                              }
                            },
                            {
                              "type": "spacer",
                              "properties": {
                                "width": 8
                              }
                            },
                            {
                              "type": "text",
                              "properties": {
                                "text": "•",
                                "style": "body2"
                              }
                            },
                            {
                              "type": "spacer",
                              "properties": {
                                "width": 8
                              }
                            },
                            {
                              "type": "text",
                              "properties": {
                                "text": "{{item.timestamp}}",
                                "style": "body2"
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
                    "height": 12
                  }
                },
                {
                  "type": "text",
                  "properties": {
                    "text": "{{item.description}}",
                    "style": "body1"
                  }
                }
              ]
            },
            "actions": {
              "onClick": {
                "type": "navigation",
                "destination": "news_detail",
                "params": {
                  "newsId": "{{item.id}}",
                  "category": "{{item.category}}"
                }
              }
            }
          }
        }
      ]
    }
  },
  "theme": {
    "primaryColor": "#6200EE",
    "secondaryColor": "#03DAC5",
    "backgroundColor": "#FFFFFF",
    "textColor": "#000000"
  }
}""".trimIndent()

val simpleCards = """{
  "version": "1.0",
  "screen": {
    "id": "simple_cards_screen",
    "title": "Simple Cards",
    "layout": {
      "type": "column",
      "modifier": {
        "fillMaxSize": true,
        "padding": 16
      },
      "children": [
        {
          "type": "text",
          "properties": {
            "text": "Cards Demo",
            "style": "headline1"
          },
          "modifier": {
            "padding": 16
          }
        },
        {
          "type": "card",
          "modifier": {
            "fillMaxWidth": true,
            "padding": 8
          },
          "children": [
            {
              "type": "column",
              "modifier": {
                "padding": 16
              },
              "children": [
                {
                  "type": "text",
                  "properties": {
                    "text": "Welcome Card",
                    "style": "headline2"
                  }
                },
                {
                  "type": "text",
                  "properties": {
                    "text": "This is a simple card example.",
                    "style": "body1"
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
            "padding": 8
          },
          "children": [
            {
              "type": "row",
              "modifier": {
                "padding": 16
              },
              "children": [
                {
                  "type": "text",
                  "properties": {
                    "text": "Status:",
                    "style": "subtitle1"
                  }
                },
                {
                  "type": "spacer",
                  "properties": {
                    "width": 8
                  }
                },
                {
                  "type": "text",
                  "properties": {
                    "text": "Active",
                    "style": "body1"
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
            "padding": 8
          },
          "children": [
            {
              "type": "column",
              "modifier": {
                "padding": 16
              },
              "children": [
                {
                  "type": "text",
                  "properties": {
                    "text": "Action Card",
                    "style": "headline2"
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
                    "text": "Click the button below to perform an action.",
                    "style": "body1"
                  }
                },
                {
                  "type": "spacer",
                  "properties": {
                    "height": 16
                  }
                },
                {
                  "type": "button",
                  "properties": {
                    "text": "Click Me",
                    "enabled": true
                  },
                  "modifier": {
                    "fillMaxWidth": true
                  },
                  "actions": {
                    "onClick": {
                      "type": "action",
                      "destination": "button_clicked"
                    }
                  }
                }
              ]
            }
          ]
        }
      ]
    }
  }
}""".trimIndent()

val scrollableCards = """{
  "version": "1.0",
  "screen": {
    "id": "scrollable_cards_screen",
    "title": "Scrollable Cards Demo",
    "layout": {
      "type": "column",
      "modifier": {
        "fillMaxSize": true
      },
      "children": [
        {
          "type": "text",
          "properties": {
            "text": "Cards Demo",
            "style": "headline1"
          },
          "modifier": {
            "padding": 16
          }
        },
        {
          "type": "lazy_column",
          "modifier": {
            "fillMaxWidth": true,
            "weight": 1
          },
          "dataSource": {
            "type": "static",
            "items": [
              {
                "id": "welcome_card",
                "type": "simple",
                "title": "Welcome Card",
                "description": "This is a simple welcome card with some introductory text. Cards are great for organizing content in a clean, material design way."
              },
              {
                "id": "profile_card",
                "type": "profile",
                "name": "John Doe",
                "role": "Software Engineer",
                "bio": "Building amazing mobile applications with passion and precision. Always learning new technologies.",
                "imageUrl": "https://picsum.photos/100/100?random=10"
              },
              {
                "id": "metrics_card",
                "type": "metrics",
                "title": "Dashboard Metrics",
                "sales": "${'$'}24,560",
                "salesChange": "+12%",
                "users": "1,247",
                "usersChange": "+8%"
              },
              {
                "id": "action_card",
                "type": "action",
                "title": "Get Started",
                "description": "Ready to begin your journey? Click the button below to start exploring our features and unlock amazing possibilities.",
                "primaryAction": "Get Started",
                "secondaryAction": "Learn More"
              },
              {
                "id": "news_1",
                "type": "news",
                "title": "New Feature Release",
                "subtitle": "Dark mode support added",
                "description": "Experience our app in a whole new way with our beautiful dark theme. Perfect for late-night browsing and easier on the eyes.",
                "imageUrl": "https://picsum.photos/200/200?random=20",
                "category": "Product",
                "timestamp": "2 hours ago"
              },
              {
                "id": "news_2",
                "type": "news",
                "title": "Performance Improvements",
                "subtitle": "50% faster loading times",
                "description": "We've optimized our backend infrastructure to deliver lightning-fast performance across all devices and platforms.",
                "imageUrl": "https://picsum.photos/200/200?random=21",
                "category": "Engineering",
                "timestamp": "1 day ago"
              },
              {
                "id": "news_3",
                "type": "news",
                "title": "Community Milestone",
                "subtitle": "100K active users reached!",
                "description": "Thank you to our amazing community for helping us reach this incredible milestone. Here's to the next 100K users!",
                "imageUrl": "https://picsum.photos/200/200?random=22",
                "category": "Community",
                "timestamp": "3 days ago"
              },
              {
                "id": "status_card",
                "type": "status",
                "title": "System Status",
                "status": "All Systems Operational",
                "uptime": "99.9%",
                "lastUpdate": "5 minutes ago"
              },
              {
                "id": "notification_card",
                "type": "simple",
                "title": "Notifications",
                "description": "You have 3 new notifications waiting for you. Tap here to view them and stay up to date with the latest updates."
              },
              {
                "id": "settings_card",
                "type": "action",
                "title": "Settings & Preferences",
                "description": "Customize your experience by adjusting your preferences, notification settings, and privacy controls.",
                "primaryAction": "Open Settings",
                "secondaryAction": "Privacy"
              }
            ]
          },
          "itemTemplate": {
            "type": "card",
            "layout": {
              "type": "column",
              "modifier": {
                "fillMaxWidth": true,
                "padding": 12
              },
              "children": [
                {
                  "type": "column",
                  "modifier": {
                    "padding": 16
                  },
                  "children": [
                    {
                      "type": "text",
                      "properties": {
                        "text": "{{item.title}}",
                        "style": "headline2"
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
                        "text": "{{item.description}}",
                        "style": "body1"
                      }
                    },
                    {
                      "type": "spacer",
                      "properties": {
                        "height": 12
                      }
                    },
                    {
                      "type": "text",
                      "properties": {
                        "text": "Card Type: {{item.type}} | ID: {{item.id}}",
                        "style": "body2"
                      }
                    }
                  ]
                }
              ]
            },
            "actions": {
              "onClick": {
                "type": "navigation",
                "destination": "card_detail",
                "params": {
                  "cardId": "{{item.id}}",
                  "cardType": "{{item.type}}"
                }
              }
            }
          }
        }
      ]
    }
  },
  "theme": {
    "primaryColor": "#6200EE",
    "secondaryColor": "#03DAC5",
    "backgroundColor": "#FFFFFF",
    "textColor": "#000000"
  }
}""".trimIndent()