plugins {
    java
    `maven-publish`
}

group = "com.jiangyc"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jsoup:jsoup:1.14.3")
    implementation("org.slf4j:slf4j-api:2.0.0-alpha7")
    implementation("org.slf4j:slf4j-simple:2.0.0-alpha7")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_11.toString()
    targetCompatibility = JavaVersion.VERSION_11.toString()

    options.encoding = "UTF-8"
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/JSpringYC/reptile-core")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}