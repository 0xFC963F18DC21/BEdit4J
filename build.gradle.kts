import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.31"
    id("org.jmailen.kotlinter") version "3.4.0"
    application
}

group = "net.nergi"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
    testImplementation("org.junit.platform:junit-platform-launcher:1.2.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

val mainLoc = "MainKt"

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = mainLoc
        attributes["Implementation-Version"] = archiveVersion
    }

    from ({
        configurations.runtimeClasspath.get().filter {
            it.name.endsWith("jar")
        }.map { zipTree(it) }
    })
}

application {
    mainClassName = mainLoc
}
