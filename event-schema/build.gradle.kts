plugins {
    kotlin("jvm")
}

java.sourceCompatibility = JavaVersion.VERSION_11

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.4")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-joda:2.11.4")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}
