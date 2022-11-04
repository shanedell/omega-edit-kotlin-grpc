plugins {
    kotlin("jvm") version "1.6.10"
    id("com.google.protobuf") version "0.8.18" apply false
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
}

group = "com.sdell"
version = "1.0-SNAPSHOT"
ext["grpcVersion"] = "1.50.2"
ext["grpcKotlinVersion"] = "1.3.0" // CURRENT_GRPC_KOTLIN_VERSION
ext["protobufVersion"] = "3.21.9"
ext["coroutinesVersion"] = "1.6.2"
ext["omegaEditVersion"] = "0.9.23"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    testImplementation(kotlin("test"))
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }

    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}
