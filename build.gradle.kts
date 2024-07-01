import org.openjdk.jmh.results.format.ResultFormat

plugins {
    id("java")
    id("me.champeau.jmh") version "0.7.2"
    application
}

group = "org.alexeev"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("Main")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.openjdk.jmh:jmh-core:1.37")
    testImplementation("org.openjdk.jmh:jmh-generator-annprocess:1.37")
    implementation("org.jetbrains:annotations:24.1.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes(mapOf("Main-Class" to "Main"))
    }
}

jmh {
    resultFormat = "csv"
}