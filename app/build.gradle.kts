/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/6.7/userguide/building_java_projects.html
 */

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    // Use JCenter for resolving dependencies.
    jcenter()
    //mavenCentral()
}

dependencies {
    // Use JUnit test framework.
    testImplementation("junit:junit:4.13.1")
    testImplementation("org.hamcrest:hamcrest-core:1.3")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:29.0-jre")

    // JMH for benchmarks
    implementation("org.openjdk.jmh:jmh-core:1.26")
    annotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.26")

    // Log4j
    implementation("org.apache.logging.log4j:log4j-api:2.14.0")
    implementation("org.apache.logging.log4j:log4j-core:2.14.0")
}

application {
    // Define the main class for the application.
    mainClass.set("ReadWriteRegisterMutexes.App")
}

// Increase JVM heap size when running project
tasks.withType<JavaExec>().configureEach {
    jvmArgs = listOf("-Xms1G", "-Xmx20G")
}

// Increase JVM heap size when running tests
tasks.test {
    minHeapSize = "1G"
    maxHeapSize = "20G"
}
