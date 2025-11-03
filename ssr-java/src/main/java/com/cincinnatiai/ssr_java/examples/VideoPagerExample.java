package com.cincinnatiai.ssr_java.examples;

import com.cincinnatiai.ssr_java.SSR;
import com.cincinnatiai.ssr_java.model.NodeModel;

import java.util.Arrays;

import static com.cincinnatiai.ssr_java.SSR.*;

/**
 * Example demonstrating the use of VideoItem and HorizontalPager components.
 * This creates a swipeable pager with video items.
 */
public class VideoPagerExample {
    public static void main(String[] args) {
        NodeModel screen = createVideoPagerScreen();
        System.out.println("Video Pager Example JSON:");
        System.out.println(SSR.toJson(screen));
    }

    public static NodeModel createVideoPagerScreen() {
        return scaffold()
                .topBar(topAppBar("Video Gallery"))
                .content(
                        column()
                                .modifier(modifier().fillMaxSize().padding(16))
                                .addChild(
                                        text("Swipe to explore videos")
                                                .textStyle(textStyle()
                                                        .fontSize(18f)
                                                        .fontWeight("bold")
                                                        .color("#212121"))
                                                .modifier(modifier().paddingBottom(16))
                                )
                                .addChild(
                                        horizontalPager()
                                                .modifier(modifier().height(400))
                                                .children(Arrays.asList(
                                                        videoItem(
                                                                "Introduction to Server-Side Rendering",
                                                                "Learn the basics of SSR and how it improves app performance and user experience.",
                                                                "https://via.placeholder.com/120",
                                                                "video_1_clicked"
                                                        ).build(),
                                                        videoItem(
                                                                "Advanced SSR Techniques",
                                                                "Dive deeper into advanced topics including state management and optimization strategies.",
                                                                "https://via.placeholder.com/120",
                                                                "video_2_clicked",
                                                                4f,
                                                                12f,
                                                                "#E3F2FD"
                                                        ).build(),
                                                        videoItem(
                                                                "Best Practices & Patterns",
                                                                "Expert tips on building scalable applications with SSR architecture.",
                                                                "https://via.placeholder.com/120",
                                                                "video_3_clicked"
                                                        ).backgroundColor("#F3E5F5").build()
                                                ))
                                )
                )
                .build();
    }

    public static NodeModel createSimpleVideoList() {
        return scaffold()
                .topBar(topAppBar("Video List"))
                .content(
                        column()
                                .modifier(modifier().fillMaxSize().padding(16))
                                .addChild(
                                        videoItem(
                                                "Introduction to Android Development",
                                                "Start your journey into Android app development with this comprehensive guide.",
                                                "https://via.placeholder.com/120",
                                                "android_intro_clicked"
                                        )
                                )
                                .addChild(
                                        spacer(16)
                                )
                                .addChild(
                                        videoItem(
                                                "Kotlin for Beginners",
                                                "Learn Kotlin programming language from scratch with practical examples.",
                                                "https://via.placeholder.com/120",
                                                "kotlin_basics_clicked"
                                        )
                                )
                                .addChild(
                                        spacer(16)
                                )
                                .addChild(
                                        videoItem(
                                                "Jetpack Compose Tutorial",
                                                "Master modern Android UI development with Jetpack Compose.",
                                                "https://via.placeholder.com/120",
                                                "compose_tutorial_clicked",
                                                6f,
                                                16f,
                                                "#E8F5E9"
                                        )
                                )
                )
                .build();
    }
}
