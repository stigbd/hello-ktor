val kotlinVersion: String by project
val logbackVersion: String by project
val ktorVersion: String by project

plugins {
    application
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.serialization").version("2.0.0")
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1" // Apply ktlint plugin
    id("org.jetbrains.kotlinx.kover") version "0.9.0-RC"
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories { mavenCentral() }

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-server-config-yaml:$ktorVersion")
    implementation("io.ktor:ktor-server-openapi:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    testImplementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    testImplementation("io.ktor:ktor-server-test-host-jvm:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    testImplementation("org.testcontainers:postgresql:1.16.0")
}

kover {
    reports {
        filters {
            excludes {
                // exclusion rules - classes to exclude from report
                classes("com.example.ApplicationKt")
            }
        }
        verify {
            // verification rules
            rule {
                minBound(100)
            }
        }
    }
}
