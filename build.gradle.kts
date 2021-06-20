group = "dev.rudge"
version = "1.0-SNAPSHOT"

val javalinVersion = "3.8.0"
val slf4jVersion = "1.7.28"
val koinVersion = "1.0.2"
val swaggerVersion = "3.24.3"
val swaggerCoreVersion = "2.0.9"
val unirestVersion = "1.4.9"

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.5.10"

    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    jcenter()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("io.javalin:javalin:$javalinVersion")
    implementation("org.slf4j:slf4j-simple:$slf4jVersion")
    implementation("org.koin:koin-core:$koinVersion")
    implementation("org.webjars:swagger-ui:$swaggerVersion")
    implementation("io.swagger.core.v3:swagger-core:$swaggerCoreVersion")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    
    testImplementation("io.mockk:mockk:1.10.2")
}