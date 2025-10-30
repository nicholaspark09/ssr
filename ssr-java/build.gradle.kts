plugins {
    id("java-library")
    `maven-publish`
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
}

val major = 0
val minor = 0
val patch = 1

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            groupId = "com.cincinnatiai"
            artifactId = "ssr-java"
            version = "${major}.${minor}.${patch}"

            pom {
                name.set("SSR Java Library")
                description.set("Server-side rendering library for building JSON UI definitions in Java")
                url.set("https://github.com/nicholaspark09/ssr")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        id.set("nicholaspark09")
                        name.set("Nicholas Park")
                        email.set("nicholaspark09@gmail.com")
                    }
                }
            }
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/nicholaspark09/ssr")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
}
