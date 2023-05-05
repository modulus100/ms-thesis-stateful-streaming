/*
 * This file was generated by the Gradle 'init' task.
 *
 * This project uses @Incubating APIs which are subject to change.
 */

plugins {
    id("ms.thesis.java-application-conventions")
    kotlin("jvm") version "1.8.20"
    kotlin("plugin.spring") version "1.8.20"
    id("org.springframework.boot") version "3.0.6"
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:1.8.20")

    implementation("org.springframework.boot:spring-boot-starter-webflux:3.0.6")
    implementation("io.projectreactor.kafka:reactor-kafka:1.3.17")
    implementation("org.springframework.kafka:spring-kafka:3.0.6")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    implementation("com.dynatrace.hash4j:hash4j:0.9.0")
    implementation("io.github.oshai:kotlin-logging-jvm:4.0.0-beta-22")

    implementation(project(":common"))
}

application {
    // Define the main class for the application.
    mainClass.set("ms.thesis.coroutines.GeneratorOnCoroutinesAppKt")
}
