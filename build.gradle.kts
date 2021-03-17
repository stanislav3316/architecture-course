import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.3" apply false
    id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
    id("com.dorongold.task-tree") version "1.5" apply false
    // id("org.jlleitschuh.gradle.ktlint") version "9.2.1" apply false
    kotlin("jvm") version "1.4.30" apply false
    kotlin("plugin.spring") version "1.4.30" apply false
    kotlin("plugin.jpa") version "1.4.30" apply false
    kotlin("plugin.allopen") version "1.4.30" apply false
    kotlin("kapt") version "1.4.30" apply false
}

allprojects {

    group = "com.uberpopug"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    tasks {
        withType<Test> {
            useJUnitPlatform()
            finalizedBy("jacocoTestReport")
        }

        withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "11"
                allWarningsAsErrors = true
            }
        }

        withType<org.springframework.boot.gradle.tasks.run.BootRun> {
            jvmArgs = listOf(
                "-Xms128m", "-Xmx256m", "-XX:+PrintFlagsFinal", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseZGC"
            )
        }
    }
}
