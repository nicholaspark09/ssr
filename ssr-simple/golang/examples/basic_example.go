package main

import (
	"fmt"
	"log"

	ssr "path/to/your/module/ssr_simple" // Update this import path
)

func main() {
	// Example 1: Simple Card
	simpleCard := ssr.Card(
		"Welcome",
		"This is a simple card example",
		4.0,
		ssr.Modifier().WithFillMaxWidth().WithPadding(16),
	)

	json, err := simpleCard.ToJSONIndent()
	if err != nil {
		log.Fatal(err)
	}
	fmt.Println("=== Simple Card ===")
	fmt.Println(json)
	fmt.Println()

	// Example 2: Full Screen with Scaffold
	dashboard := ssr.Scaffold(
		"Dashboard",
		ssr.LazyColumn(
			[]ssr.NodeModel{
				*ssr.Text(
					"Welcome Back!",
					ssr.HeadingStyle(),
					ssr.Modifier().WithPaddingBottom(16),
				),
				*ssr.Row(
					[]ssr.NodeModel{
						*ssr.Card("42", "Tasks", 2.0, ssr.Modifier().WithFillMaxWidth().WithPaddingEnd(8)),
						*ssr.Card("18", "Projects", 2.0, ssr.Modifier().WithFillMaxWidth()),
					},
					ssr.Modifier().WithFillMaxWidth().WithPaddingBottom(16),
				),
				*ssr.Button(
					"View All Tasks",
					"navigate:tasks",
					ssr.Modifier().WithFillMaxWidth(),
				),
			},
			ssr.Modifier().WithFillMaxSize().WithPadding(16),
		),
	)

	json, err = dashboard.ToJSONIndent()
	if err != nil {
		log.Fatal(err)
	}
	fmt.Println("=== Dashboard Screen ===")
	fmt.Println(json)
	fmt.Println()

	// Example 3: Card with Custom Children
	productCard := ssr.CardWithChildren(
		[]ssr.NodeModel{
			*ssr.Image(
				"https://picsum.photos/400/250",
				0, 0, // Natural size
				"Product image",
				ssr.Modifier().WithFillMaxWidth(),
			),
			*ssr.Column(
				[]ssr.NodeModel{
					*ssr.Text(
						"Premium Laptop",
						ssr.TitleStyle(),
						nil,
					),
					*ssr.Text(
						"High-performance laptop for professionals",
						ssr.BodyStyle(),
						ssr.Modifier().WithPaddingTop(4),
					),
					*ssr.Box(
						[]ssr.NodeModel{
							*ssr.Button(
								"Buy Now",
								"buy:laptop_001",
								nil,
							),
						},
						ssr.Modifier().
							WithFillMaxWidth().
							WithContentAlignment("centerend").
							WithPaddingTop(12),
					),
				},
				ssr.Modifier().WithPadding(16),
			),
		},
		4.0,
		ssr.Modifier().WithFillMaxWidth(),
	)

	json, err = productCard.ToJSONIndent()
	if err != nil {
		log.Fatal(err)
	}
	fmt.Println("=== Product Card ===")
	fmt.Println(json)
}
