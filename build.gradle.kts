plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2" // For creating a fat JAR
}

group = "org.example"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // Testing
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // AWS SDK and Lambda dependencies
    implementation("software.amazon.awssdk:dynamodb-enhanced:2.20.0")
    implementation("software.amazon.awssdk:dynamodb:2.20.0")
    implementation("com.amazonaws:aws-lambda-java-core:1.2.1")
    implementation("com.amazonaws:aws-lambda-java-events:3.11.0")

    // Logging
    implementation("org.slf4j:slf4j-simple:2.0.9")
}

tasks {
    test {
        useJUnitPlatform()
    }

    named("build") {
        dependsOn(named("shadowJar"))
    }

    withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
        archiveBaseName.set("dog-quiz")
        archiveVersion.set("1.0.0")
        archiveClassifier.set("")
        manifest {
            attributes(
                    "Main-Class" to "org.example.MyLambdaHandler" // Replace with your Lambda handler class name
            )
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11 // Ensure compatibility with AWS Lambda
    targetCompatibility = JavaVersion.VERSION_11
}
