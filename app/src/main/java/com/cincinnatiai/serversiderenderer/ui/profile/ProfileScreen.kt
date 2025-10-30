package com.cincinnatiai.serversiderenderer.ui.profile

import androidx.compose.runtime.Composable
import com.cincinnatiai.ssr_library.ServerSideRenderingLibrary
import com.cincinnatiai.ssr_simple.SSRSimpleLibrary

@Composable
fun ProfileScreen() {
    val renderer by lazy {
        ServerSideRenderingLibrary.getInstance().serverSideRenderer
    }

    SSRSimpleLibrary.ShowScreen(
        """
  {
  "type": "Box",
  "modifier": {
    "fillMaxSize": true
  },
  "children": [{
    "type": "LazyColumn",
    "modifier": {
      "fillMaxSize": true,
      "paddingStart": 16,
      "paddingEnd": 16,
      "paddingTop": 8
    },
    "children": [
      {
        "type": "Spacer",
        "modifier": {
          "height": 8
        }
      },
      {
        "type": "Text",
        "title": "Chef's Assistant",
        "textStyle": {
          "fontSize": 18,
          "fontWeight": "bold",
          "color": "#000000"
        },
        "modifier": {
          "paddingBottom": 8
        }
      },
      {
        "type": "Text",
        "title": "🎉 Happy First Birthday!",
        "textStyle": {
          "fontSize": 28,
          "fontWeight": "bold",
          "color": "#FF6B6B"
        },
        "modifier": {
          "paddingBottom": 8
        }
      },
      {
        "type": "Text",
        "description": "Big transition time! Welcome to whole milk and family foods.",
        "textStyle": {
          "fontSize": 14,
          "color": "#666666"
        },
        "modifier": {
          "paddingBottom": 24
        }
      },
      {
        "type": "Card",
        "backgroundColor": "#E3F2FD",
        "elevation": 2,
        "modifier": {
          "paddingBottom": 16,
          "fillMaxWidth": true
        },
        "children": [
          {
            "type": "Column",
            "modifier": {
              "padding": 16
            },
            "children": [
              {
                "type": "Text",
                "title": "🥛 Transitioning to Whole Milk",
                "textStyle": {
                  "fontSize": 20,
                  "fontWeight": "bold",
                  "color": "#1565C0"
                },
                "modifier": {
                  "paddingBottom": 12
                }
              },
              {
                "type": "Text",
                "description": "16-24 oz of whole cow's milk per day",
                "textStyle": {
                  "fontSize": 16,
                  "fontWeight": "bold"
                },
                "modifier": {
                  "paddingBottom": 8
                }
              },
              {
                "type": "Text",
                "description": "• Can transition off formula now",
                "textStyle": {
                  "fontSize": 14
                },
                "modifier": {
                  "paddingBottom": 4
                }
              },
              {
                "type": "Text",
                "description": "• Continue breastfeeding if desired (2-4 feeds/day)",
                "textStyle": {
                  "fontSize": 14
                },
                "modifier": {
                  "paddingBottom": 4
                }
              },
              {
                "type": "Text",
                "description": "• Use cups instead of bottles",
                "textStyle": {
                  "fontSize": 14
                },
                "modifier": {
                  "paddingBottom": 4
                }
              },
              {
                "type": "Text",
                "description": "⚠️ Avoid excess milk (>24 oz) to protect iron intake",
                "textStyle": {
                  "fontSize": 13,
                  "fontWeight": "bold",
                  "color": "#C62828"
                }
              }
            ]
          }
        ]
      },
      {
        "type": "Card",
        "backgroundColor": "#F3E5F5",
        "elevation": 2,
        "modifier": {
          "paddingBottom": 16,
          "fillMaxWidth": true
        },
        "children": [
          {
            "type": "Column",
            "modifier": {
              "padding": 16
            },
            "children": [
              {
                "type": "Text",
                "title": "🍽️ Family Foods",
                "textStyle": {
                  "fontSize": 20,
                  "fontWeight": "bold",
                  "color": "#6A1B9A"
                },
                "modifier": {
                  "paddingBottom": 12
                }
              },
              {
                "type": "Text",
                "description": "3 meals + 1-2 snacks per day",
                "textStyle": {
                  "fontSize": 16,
                  "fontWeight": "bold"
                },
                "modifier": {
                  "paddingBottom": 8
                }
              },
              {
                "type": "Text",
                "description": "Your toddler can now eat most family foods!",
                "textStyle": {
                  "fontSize": 14,
                  "color": "#666666"
                },
                "modifier": {
                  "paddingBottom": 16
                }
              },
              {
                "type": "Text",
                "title": "Meal Ideas:",
                "textStyle": {
                  "fontSize": 16,
                  "fontWeight": "bold"
                },
                "modifier": {
                  "paddingBottom": 8
                }
              },
              {
                "type": "Column",
                "children": [
                  {
                    "type": "Text",
                    "description": "• Chicken, brown rice, broccoli with olive oil",
                    "textStyle": {
                      "fontSize": 13
                    },
                    "modifier": {
                      "paddingBottom": 4
                    }
                  },
                  {
                    "type": "Text",
                    "description": "• Beef meatballs, pasta, tomato sauce, peas",
                    "textStyle": {
                      "fontSize": 13
                    },
                    "modifier": {
                      "paddingBottom": 4
                    }
                  },
                  {
                    "type": "Text",
                    "description": "• Bean and cheese quesadilla with avocado",
                    "textStyle": {
                      "fontSize": 13
                    },
                    "modifier": {
                      "paddingBottom": 4
                    }
                  },
                  {
                    "type": "Text",
                    "description": "• Salmon, mashed potatoes, soft carrots",
                    "textStyle": {
                      "fontSize": 13
                    },
                    "modifier": {
                      "paddingBottom": 4
                    }
                  },
                  {
                    "type": "Text",
                    "description": "• Tofu stir-fry (very soft) with noodles",
                    "textStyle": {
                      "fontSize": 13
                    },
                    "modifier": {
                      "paddingBottom": 4
                    }
                  },
                  {
                    "type": "Text",
                    "description": "• Veggie omelet, whole-grain toast, fruit",
                    "textStyle": {
                      "fontSize": 13
                    }
                  }
                ]
              }
            ]
          }
        ]
      },
      {
        "type": "Card",
        "backgroundColor": "#FFF3E0",
        "elevation": 2,
        "modifier": {
          "paddingBottom": 16,
          "fillMaxWidth": true
        },
        "children": [
          {
            "type": "Column",
            "modifier": {
              "padding": 16
            },
            "children": [
              {
                "type": "Text",
                "title": "🍎 Snack Ideas",
                "textStyle": {
                  "fontSize": 20,
                  "fontWeight": "bold",
                  "color": "#E65100"
                },
                "modifier": {
                  "paddingBottom": 12
                }
              },
              {
                "type": "Column",
                "children": [
                  {
                    "type": "Text",
                    "description": "• Apple slices (thin/soft) with cheese",
                    "textStyle": {
                      "fontSize": 13
                    },
                    "modifier": {
                      "paddingBottom": 4
                    }
                  },
                  {
                    "type": "Text",
                    "description": "• Yogurt with berries and oats",
                    "textStyle": {
                      "fontSize": 13
                    },
                    "modifier": {
                      "paddingBottom": 4
                    }
                  },
                  {
                    "type": "Text",
                    "description": "• Whole-grain crackers with hummus",
                    "textStyle": {
                      "fontSize": 13
                    },
                    "modifier": {
                      "paddingBottom": 4
                    }
                  },
                  {
                    "type": "Text",
                    "description": "• Smooth nut/seed butter on toast fingers",
                    "textStyle": {
                      "fontSize": 13
                    }
                  }
                ]
              }
            ]
          }
        ]
      },
      {
        "type": "Card",
        "backgroundColor": "#E1F5FE",
        "elevation": 2,
        "modifier": {
          "paddingBottom": 16,
          "fillMaxWidth": true
        },
        "children": [
          {
            "type": "Column",
            "modifier": {
              "padding": 16
            },
            "children": [
              {
                "type": "Text",
                "title": "💧 Hydration",
                "textStyle": {
                  "fontSize": 20,
                  "fontWeight": "bold",
                  "color": "#0277BD"
                },
                "modifier": {
                  "paddingBottom": 8
                }
              },
              {
                "type": "Text",
                "description": "Water: Up to 12 oz per day",
                "textStyle": {
                  "fontSize": 14
                },
                "modifier": {
                  "paddingBottom": 4
                }
              },
              {
                "type": "Text",
                "description": "Offer water between meals and with meals",
                "textStyle": {
                  "fontSize": 12,
                  "color": "#666666"
                }
              }
            ]
          }
        ]
      },
      {
        "type": "Card",
        "backgroundColor": "#C8E6C9",
        "elevation": 2,
        "modifier": {
          "paddingBottom": 16,
          "fillMaxWidth": true
        },
        "children": [
          {
            "type": "Column",
            "modifier": {
              "padding": 16
            },
            "children": [
              {
                "type": "Text",
                "title": "✅ New Foods Allowed!",
                "textStyle": {
                  "fontSize": 20,
                  "fontWeight": "bold",
                  "color": "#2E7D32"
                },
                "modifier": {
                  "paddingBottom": 8
                }
              },
              {
                "type": "Text",
                "description": "• Honey is now safe!",
                "textStyle": {
                  "fontSize": 14,
                  "fontWeight": "bold"
                },
                "modifier": {
                  "paddingBottom": 4
                }
              },
              {
                "type": "Text",
                "description": "• Whole cow's milk as main drink",
                "textStyle": {
                  "fontSize": 14
                },
                "modifier": {
                  "paddingBottom": 4
                }
              },
              {
                "type": "Text",
                "description": "• Most family foods (still watch choking hazards)",
                "textStyle": {
                  "fontSize": 14
                }
              }
            ]
          }
        ]
      },
      {
        "type": "Card",
        "backgroundColor": "#FFEBEE",
        "elevation": 2,
        "modifier": {
          "paddingBottom": 16,
          "fillMaxWidth": true
        },
        "children": [
          {
            "type": "Column",
            "modifier": {
              "padding": 16
            },
            "children": [
              {
                "type": "Text",
                "title": "⚠️ Still Avoid",
                "textStyle": {
                  "fontSize": 20,
                  "fontWeight": "bold",
                  "color": "#C62828"
                },
                "modifier": {
                  "paddingBottom": 8
                }
              },
              {
                "type": "Text",
                "description": "• Choking hazards: whole grapes, hot dogs (uncut), popcorn, hard candies, nuts",
                "textStyle": {
                  "fontSize": 14
                },
                "modifier": {
                  "paddingBottom": 4
                }
              },
              {
                "type": "Text",
                "description": "• Excess milk (keep under 24 oz/day)",
                "textStyle": {
                  "fontSize": 14
                },
                "modifier": {
                  "paddingBottom": 4
                }
              },
              {
                "type": "Text",
                "description": "• Added sugars and high sodium foods",
                "textStyle": {
                  "fontSize": 14
                }
              }
            ]
          }
        ]
      },
      {
        "type": "Card",
        "backgroundColor": "#FFF9C4",
        "elevation": 2,
        "modifier": {
          "fillMaxWidth": true
        },
        "children": [
          {
            "type": "Column",
            "modifier": {
              "padding": 16
            },
            "children": [
              {
                "type": "Text",
                "title": "💡 Tips for Success",
                "textStyle": {
                  "fontSize": 20,
                  "fontWeight": "bold",
                  "color": "#F57F17"
                },
                "modifier": {
                  "paddingBottom": 8
                }
              },
              {
                "type": "Text",
                "description": "• Offer varied foods from all food groups",
                "textStyle": {
                  "fontSize": 14
                },
                "modifier": {
                  "paddingBottom": 4
                }
              },
              {
                "type": "Text",
                "description": "• Let your toddler feed themselves",
                "textStyle": {
                  "fontSize": 14
                },
                "modifier": {
                  "paddingBottom": 4
                }
              },
              {
                "type": "Text",
                "description": "• Eat together as a family when possible",
                "textStyle": {
                  "fontSize": 14
                },
                "modifier": {
                  "paddingBottom": 4
                }
              },
              {
                "type": "Text",
                "description": "• Be patient with picky eating - it's normal!",
                "textStyle": {
                  "fontSize": 14
                },
                "modifier": {
                  "paddingBottom": 4
                }
              },
              {
                "type": "Text",
                "description": "• Keep introducing new foods regularly",
                "textStyle": {
                  "fontSize": 14
                }
              }
            ]
          }
        ]
      }
    ]
  }]
}

"""
    ) { }
}