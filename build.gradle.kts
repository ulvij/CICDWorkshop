import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    id("org.jetbrains.kotlinx.kover") version("0.5.1") apply(true)
    id("org.sonarqube") version("3.3") apply(true)
}

group = "digital.pashabank"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()

    extensions.configure(kotlinx.kover.api.KoverTaskExtension::class) {
        isDisabled = false
        binaryReportFile.set(file("$buildDir/custom/result.bin"))
        includes = listOf("digital.pashabank.*")
        excludes = listOf()
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

val koverReportOutputDir = layout.buildDirectory.dir("kover-reports")

tasks.koverCollectReports {
    outputDir.set(koverReportOutputDir)
}

sonarqube {
    properties {
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.organization", "kerimovscreations")
        property("sonar.projectKey", "kerimovscreations_cicdworkshop1")
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            "${rootProject.buildDir}/kover-reports/_root_.xml"
        )
    }
}