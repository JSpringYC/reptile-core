plugins {
    java
    `maven-publish`
    signing
}

group = "com.jiangyc"
version = "1.3.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jsoup:jsoup:1.14.3")

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
    val sonatypeUsername = project.findProperty("sonatype.username") as String? ?: System.getenv("USERNAME")
    val sonatypePassword = project.findProperty("sonatype.password") as String? ?: System.getenv("PASSWORD")

    repositories {
        maven {
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = sonatypeUsername
                password = sonatypePassword
            }
        }
    }
    publications {
        create<MavenPublication>("sonatype") {
            from(components["java"])

            pom {
                name.set("Reptile Core")
                description.set("HTML网页解析及特定内容获取工具。")
                url.set("https://www.jiangyc.com/reptile-core")
                licenses {
                    license {
                        name.set("GNU GENERAL PUBLIC LICENSE, Version 3")
                        url.set("https://www.gnu.org/licenses/gpl-3.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("jiangyc")
                        name.set("佳木流泉")
                        email.set("JSpringYC@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/JSpringYC/reptile-core.git")
                    developerConnection.set("scm:git:git@github.com:JSpringYC/reptile-core.git")
                    url.set("https://www.jiangyc.com/reptile-core")
                }
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(configurations.archives.get())
    sign(publishing.publications["sonatype"])
}