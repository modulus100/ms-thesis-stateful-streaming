plugins {
    id("ms.thesis.java-application-conventions")
    kotlin("jvm") version "1.8.20"
    kotlin("plugin.spring") version "1.8.20"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux:3.0.6")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")

    implementation("com.dynatrace.hash4j:hash4j:0.9.0")

    api(project(":common"))
}

application {
    // Define the main class for the application.
    mainClass.set("ms.thesis.seed.SeedManagerApp")
}