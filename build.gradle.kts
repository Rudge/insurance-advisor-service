group = "dev.rudge"
version = "1.0-SNAPSHOT"

val javalinVersion = "3.8.0"
val slf4jVersion = "1.7.28"
val koinVersion = "1.0.2"
val swaggerVersion = "3.24.3"
val swaggerCoreVersion = "2.0.9"
val unirestVersion = "1.4.9"
val jacksonVersion = "2.10.1"


plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.5.10"

    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    jcenter()
}

// The order from registering the source set up to the configuration of the component test as a runtime only
// must happen in this particular order at this point of the build configuration.
sourceSets {
    create("integrationTest") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

// Create the integration test setup for dependencies
val integrationTestImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

// Create the integration test setup for dependencies
configurations["integrationTestRuntimeOnly"].extendsFrom(configurations.runtimeOnly.get())

val componentTestTask = tasks.create("integrationTest", Test::class) {
    description = "Runs the integration tests."
    group = "verification"

    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath

    shouldRunAfter("test")
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("io.javalin:javalin:$javalinVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("org.slf4j:slf4j-simple:$slf4jVersion")
    implementation("org.koin:koin-core:$koinVersion")
    implementation("org.webjars:swagger-ui:$swaggerVersion")
    implementation("io.swagger.core.v3:swagger-core:$swaggerCoreVersion")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation("io.mockk:mockk:1.10.2")

    integrationTestImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    integrationTestImplementation("io.rest-assured:kotlin-extensions:4.3.0")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    sourceCompatibility = JavaVersion.VERSION_1_8.name
    targetCompatibility = JavaVersion.VERSION_1_8.name

    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

