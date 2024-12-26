plugins {
    id("java")
    kotlin("jvm")
}

group = "org.example"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // AWS SDK v2 for DynamoDB Enhanced Client
    implementation("software.amazon.awssdk:dynamodb-enhanced:2.20.0")

    // Core AWS SDK DynamoDB dependency
    implementation("software.amazon.awssdk:dynamodb:2.20.0")

    // SLF4J for logging (optional but recommended)
    implementation("org.slf4j:slf4j-simple:2.0.9")
    implementation(kotlin("stdlib-jdk8"))

    implementation("com.amazonaws:aws-lambda-java-core:1.2.1") // Core library for Lambda
    implementation("software.amazon.awssdk:dynamodb:2.20.0")  // DynamoDB SDK v2
    implementation("software.amazon.awssdk:dynamodb-enhanced:2.20.0") // DynamoDB Enhanced
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(16)
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
