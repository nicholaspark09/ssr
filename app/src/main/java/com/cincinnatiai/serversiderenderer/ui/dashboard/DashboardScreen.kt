package com.cincinnatiai.serversiderenderer.ui.dashboard

import androidx.compose.runtime.Composable
import com.cincinnatiai.ssr_library.ServerSideRenderingLibrary
import com.cincinnatiai.ssr_simple.SSRSimpleLibrary

@Composable
fun DashboardScreen() {
    val renderer by lazy {
        ServerSideRenderingLibrary.getInstance().serverSideRenderer
    }

    SSRSimpleLibrary.ShowScreen("""{
  "type": "Scaffold",
  "topBar": {
    "type": "TopAppBar",
    "title": "Dashboard"
  },
  "content": {
    "type": "LazyColumn",
    "modifier": {
      "fillMaxSize": true,
      "padding": 16
    },
    "children": [
      {
        "type": "Card",
        "title": "Welcome Back!",
        "description": "You have 3 new notifications and 5 pending tasks",
        "elevation": 8,
        "backgroundColor": "#1976D2",
        "modifier": {
          "fillMaxWidth": true,
          "paddingBottom": 16
        }
      },
      {
        "type": "Text",
        "title": "Quick Actions",
        "textStyle": {
          "fontSize": 20,
          "fontWeight": "bold",
          "color": "#212121"
        },
        "modifier": {
          "paddingBottom": 12
        }
      },
      {
        "type": "Row",
        "modifier": {
          "fillMaxWidth": true,
          "paddingBottom": 16,
          "verticalAlignment": "center"
        },
        "children": [
          {
            "type": "Button",
            "label": "Create",
            "action": "navigate:create",
            "modifier": {
              "paddingEnd": 8
            }
          },
          {
            "type": "Button",
            "label": "Upload",
            "action": "navigate:upload",
            "modifier": {
              "paddingEnd": 8
            }
          },
          {
            "type": "Button",
            "label": "Share",
            "action": "navigate:share"
          }
        ]
      },
      {
        "type": "Text",
        "title": "Recent Activity",
        "textStyle": {
          "fontSize": 20,
          "fontWeight": "bold",
          "color": "#212121"
        },
        "modifier": {
          "paddingBottom": 12
        }
      },
      {
        "type": "Card",
        "title": "Project Alpha",
        "description": "Last updated 2 hours ago",
        "elevation": 4,
        "backgroundColor": "#E8F5E9",
        "modifier": {
          "fillMaxWidth": true,
          "paddingBottom": 12
        }
      },
      {
        "type": "Card",
        "title": "Meeting Notes",
        "description": "Quarterly review - Team discussed goals and achievements",
        "elevation": 4,
        "backgroundColor": "#FFF3E0",
        "modifier": {
          "fillMaxWidth": true,
          "paddingBottom": 12
        }
      },
      {
        "type": "Card",
        "title": "Design Review",
        "description": "Reviewed new UI mockups with design team",
        "elevation": 4,
        "backgroundColor": "#F3E5F5",
        "modifier": {
          "fillMaxWidth": true,
          "paddingBottom": 16
        }
      },
      {
        "type": "Text",
        "title": "Statistics",
        "textStyle": {
          "fontSize": 20,
          "fontWeight": "bold",
          "color": "#212121"
        },
        "modifier": {
          "paddingBottom": 12
        }
      },
      {
        "type": "Row",
        "modifier": {
          "fillMaxWidth": true,
          "paddingBottom": 16,
          "verticalAlignment": "top"
        },
        "children": [
          {
            "type": "Card",
            "title": "42",
            "description": "Tasks",
            "elevation": 2,
            "backgroundColor": "#FFEBEE",
            "modifier": {
              "fillMaxWidth": true,
              "paddingEnd": 8
            }
          },
          {
            "type": "Card",
            "title": "18",
            "description": "Projects",
            "elevation": 2,
            "backgroundColor": "#E3F2FD",
            "modifier": {
              "fillMaxWidth": true
            }
          }
        ]
      },
      {
        "type": "Box",
        "modifier": {
          "fillMaxWidth": true,
          "backgroundColor": "#ECEFF1",
          "padding": 24,
          "contentAlignment": "center"
        },
        "children": [
          {
            "type": "Column",
            "modifier": {
              "horizontalAlignment": "center"
            },
            "children": [
              {
                "type": "Text",
                "title": "Need Help?",
                "textStyle": {
                  "fontSize": 18,
                  "fontWeight": "medium",
                  "color": "#37474F"
                }
              },
              {
                "type": "Text",
                "description": "Contact support for assistance",
                "textStyle": {
                  "fontSize": 14,
                  "color": "#546E7A"
                },
                "modifier": {
                  "paddingTop": 4,
                  "paddingBottom": 12
                }
              },
              {
                "type": "Button",
                "label": "Contact Support",
                "action": "navigate:support"
              }
            ]
          }
        ]
      },
      {
        "type": "Text",
        "title": "Team Updates",
        "textStyle": {
          "fontSize": 20,
          "fontWeight": "bold",
          "color": "#212121"
        },
        "modifier": {
          "paddingTop": 16,
          "paddingBottom": 12
        }
      },
      {
        "type": "Card",
        "elevation": 6,
        "backgroundColor": "#FFFFFF",
        "modifier": {
          "fillMaxWidth": true,
          "paddingBottom": 12
        },
        "children": [
          {
            "type": "Column",
            "modifier": {
              "fillMaxWidth": true,
              "padding": 16
            },
            "children": [
              {
                "type": "Row",
                "modifier": {
                  "fillMaxWidth": true,
                  "verticalAlignment": "center"
                },
                "children": [
                  {
                    "type": "Column",
                    "modifier": {
                      "fillMaxWidth": true
                    },
                    "children": [
                      {
                        "type": "Text",
                        "title": "Sarah Johnson",
                        "textStyle": {
                          "fontSize": 16,
                          "fontWeight": "bold"
                        }
                      },
                      {
                        "type": "Text",
                        "description": "Completed the homepage redesign",
                        "textStyle": {
                          "fontSize": 14,
                          "color": "#616161"
                        }
                      }
                    ]
                  }
                ]
              },
              {
                "type": "Row",
                "modifier": {
                  "fillMaxWidth": true,
                  "paddingTop": 8,
                  "verticalAlignment": "center"
                },
                "children": [
                  {
                    "type": "Box",
                    "modifier": {
                      "fillMaxWidth": true,
                      "contentAlignment": "centerend"
                    },
                    "children": [
                      {
                        "type": "Button",
                        "label": "View Details",
                        "action": "navigate:details"
                      }
                    ]
                  }
                ]
              }
            ]
          }
        ]
      },
      {
        "type": "Card",
        "elevation": 6,
        "backgroundColor": "#FFFFFF",
        "modifier": {
          "fillMaxWidth": true,
          "paddingBottom": 12
        },
        "children": [
          {
            "type": "Column",
            "modifier": {
              "fillMaxWidth": true,
              "padding": 16
            },
            "children": [
              {
                "type": "Row",
                "modifier": {
                  "fillMaxWidth": true,
                  "verticalAlignment": "center"
                },
                "children": [
                  {
                    "type": "Column",
                    "modifier": {
                      "fillMaxWidth": true
                    },
                    "children": [
                      {
                        "type": "Text",
                        "title": "Mike Chen",
                        "textStyle": {
                          "fontSize": 16,
                          "fontWeight": "bold"
                        }
                      },
                      {
                        "type": "Text",
                        "description": "Fixed critical bug in authentication flow",
                        "textStyle": {
                          "fontSize": 14,
                          "color": "#616161"
                        }
                      }
                    ]
                  }
                ]
              },
              {
                "type": "Row",
                "modifier": {
                  "fillMaxWidth": true,
                  "paddingTop": 8,
                  "verticalAlignment": "center"
                },
                "children": [
                  {
                    "type": "Box",
                    "modifier": {
                      "fillMaxWidth": true,
                      "contentAlignment": "centerend"
                    },
                    "children": [
                      {
                        "type": "Button",
                        "label": "View Details",
                        "action": "navigate:details"
                      }
                    ]
                  }
                ]
              }
            ]
          }
        ]
      },
      {
        "type": "Row",
        "modifier": {
          "fillMaxWidth": true,
          "backgroundColor": "#C8E6C9",
          "padding": 16,
          "verticalAlignment": "center"
        },
        "children": [
          {
            "type": "Column",
            "modifier": {
              "fillMaxWidth": true
            },
            "children": [
              {
                "type": "Text",
                "title": "All systems operational",
                "textStyle": {
                  "fontSize": 16,
                  "fontWeight": "medium",
                  "color": "#2E7D32"
                }
              },
              {
                "type": "Text",
                "description": "Last checked: Just now",
                "textStyle": {
                  "fontSize": 12,
                  "color": "#388E3C"
                }
              }
            ]
          }
        ]
      },
      {
        "type": "Box",
        "modifier": {
          "fillMaxWidth": true,
          "padding": 32,
          "contentAlignment": "center"
        },
        "children": [
          {
            "type": "Column",
            "modifier": {
              "horizontalAlignment": "center"
            },
            "children": [
              {
                "type": "Text",
                "title": "End of Dashboard",
                "textStyle": {
                  "fontSize": 14,
                  "color": "#9E9E9E"
                }
              },
              {
                "type": "Button",
                "label": "Refresh",
                "action": "navigate:refresh",
                "modifier": {
                  "paddingTop": 12
                }
              }
            ]
          }
        ]
      }
    ]
  }
}
""") { }
}