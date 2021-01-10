import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    java
    application
}

repositories {
    jcenter()
}

dependencies {
    implementation("org.json:json:20201115")
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
    testImplementation("org.junit.platform:junit-platform-runner:1.7.0")
}

tasks.withType<Test> {
    useJUnitPlatform()

    testLogging {
        events(TestLogEvent.FAILED, TestLogEvent.STANDARD_ERROR, TestLogEvent.SKIPPED, TestLogEvent.PASSED)
        exceptionFormat = TestExceptionFormat.FULL
        showExceptions = true
        showCauses = true
        showStackTraces = true
    }
}

application {
    mainClass.set("com.pipan.elephant.App")
}

group = "com.pipan.elephant"
version = "0.0.1"
description = "CLI for deploying PHP applications"
java.sourceCompatibility = JavaVersion.VERSION_1_8
