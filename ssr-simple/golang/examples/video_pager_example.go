package main

import (
	"fmt"
	"log"

	ssr "path/to/your/module/ssr_simple" // Update this import path
)

func main() {
	// Example 1: Simple VideoItem
	videoItem := ssr.VideoItemSimple(
		"Introduction to Go",
		"Learn the basics of Go programming in this comprehensive introduction video.",
		"https://picsum.photos/120",
		"play:video_001",
		ssr.Modifier().WithFillMaxWidth().WithPadding(16),
	)

	json, err := videoItem.ToJSONIndent()
	if err != nil {
		log.Fatal(err)
	}
	fmt.Println("=== Simple Video Item ===")
	fmt.Println(json)
	fmt.Println()

	// Example 2: VideoItem with custom styling
	customVideoItem := ssr.VideoItem(
		"Advanced Go Patterns",
		"Deep dive into advanced Go patterns and best practices used by experts.",
		"https://picsum.photos/120",
		"play:video_002",
		8.0,   // elevation
		16.0,  // rounded corners
		"#E3F2FD", // background color
		ssr.Modifier().WithFillMaxWidth().WithPadding(16),
	)

	json, err = customVideoItem.ToJSONIndent()
	if err != nil {
		log.Fatal(err)
	}
	fmt.Println("=== Custom Video Item ===")
	fmt.Println(json)
	fmt.Println()

	// Example 3: HorizontalPager with VideoItems
	videoList := ssr.Scaffold(
		"Video Library",
		ssr.Column(
			[]ssr.NodeModel{
				*ssr.Text(
					"Featured Videos",
					ssr.HeadingStyle(),
					ssr.Modifier().WithPadding(16),
				),
				*ssr.HorizontalPager(
					[]ssr.NodeModel{
						*ssr.VideoItemSimple(
							"Video 1: Introduction",
							"Learn the basics in this introductory video covering all essential concepts.",
							"https://picsum.photos/120/120",
							"play:video_001",
							ssr.Modifier().WithFillMaxWidth().WithPadding(16),
						),
						*ssr.VideoItemSimple(
							"Video 2: Advanced Topics",
							"Dive deeper into advanced topics and best practices for professionals.",
							"https://picsum.photos/120/120",
							"play:video_002",
							ssr.Modifier().WithFillMaxWidth().WithPadding(16),
						),
						*ssr.VideoItemSimple(
							"Video 3: Expert Tips",
							"Expert tips and tricks from industry professionals.",
							"https://picsum.photos/120/120",
							"play:video_003",
							ssr.Modifier().WithFillMaxWidth().WithPadding(16),
						),
					},
					ssr.Modifier().WithFillMaxWidth().WithHeight(200),
				),
				*ssr.Text(
					"Swipe to see more videos",
					ssr.BodyStyle(),
					ssr.Modifier().WithPadding(16),
				),
			},
			ssr.Modifier().WithFillMaxSize(),
		),
	)

	json, err = videoList.ToJSONIndent()
	if err != nil {
		log.Fatal(err)
	}
	fmt.Println("=== Video Library with HorizontalPager ===")
	fmt.Println(json)
	fmt.Println()

	// Example 4: Mixed content in HorizontalPager
	mixedPager := ssr.Scaffold(
		"Course Overview",
		ssr.Column(
			[]ssr.NodeModel{
				*ssr.HorizontalPager(
					[]ssr.NodeModel{
						*ssr.VideoItem(
							"Course Introduction",
							"Welcome to the course! Watch this intro video to get started.",
							"https://picsum.photos/120",
							"play:intro",
							4.0,
							12.0,
							"#E8F5E9",
							ssr.Modifier().WithFillMaxWidth().WithPadding(16),
						),
						*ssr.CardWithChildren(
							[]ssr.NodeModel{
								*ssr.Column(
									[]ssr.NodeModel{
										*ssr.Text(
											"Course Materials",
											ssr.TitleStyle(),
											nil,
										),
										*ssr.Text(
											"Download all course materials, including PDFs, code samples, and additional resources.",
											ssr.BodyStyle(),
											ssr.Modifier().WithPaddingTop(8),
										),
										*ssr.Button(
											"Download Materials",
											"download:materials",
											ssr.Modifier().WithFillMaxWidth().WithPaddingTop(16),
										),
									},
									ssr.Modifier().WithPadding(16),
								),
							},
							4.0,
							ssr.Modifier().WithFillMaxWidth().WithPadding(16),
						),
						*ssr.VideoItem(
							"Q&A Session",
							"Watch the recorded Q&A session with answers to common questions.",
							"https://picsum.photos/120",
							"play:qa",
							4.0,
							12.0,
							"#F3E5F5",
							ssr.Modifier().WithFillMaxWidth().WithPadding(16),
						),
					},
					ssr.Modifier().WithFillMaxWidth().WithHeight(300),
				),
			},
			ssr.Modifier().WithFillMaxSize(),
		),
	)

	json, err = mixedPager.ToJSONIndent()
	if err != nil {
		log.Fatal(err)
	}
	fmt.Println("=== Mixed Content HorizontalPager ===")
	fmt.Println(json)
}
