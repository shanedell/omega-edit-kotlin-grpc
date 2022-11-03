plugins {
    application
    kotlin("jvm")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

repositories {
    mavenCentral()
    mavenLocal()
    maven {
        url = uri("https://maven.pkg.github.com/ctc-oss/omega-edit")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_USER")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    implementation(project(":stub"))
    implementation("io.grpc:grpc-netty:${rootProject.ext["grpcVersion"]}")
    implementation("io.grpc:grpc-protobuf:${rootProject.ext["grpcVersion"]}")

    implementation("io.grpc:grpc-stub:${rootProject.ext["grpcVersion"]}")
    implementation("io.grpc:grpc-kotlin-stub:1.3.0")
    implementation("com.google.protobuf:protobuf-kotlin:${rootProject.ext["protobufVersion"]}")
    implementation("com.google.protobuf:protobuf-java:${rootProject.ext["protobufVersion"]}")

    implementation("io.arrow-kt:arrow-core:1.1.2")

    implementation("com.ctc:omega-edit-spi_2.13:${rootProject.ext["omegaEditVersion"]}")
    implementation("com.ctc:omega-edit-native_2.13:${rootProject.ext["omegaEditVersion"]}:macos-64")
    implementation("com.ctc:omega-edit_2.13:${rootProject.ext["omegaEditVersion"]}")
}

tasks.register<JavaExec>("OmegaEditServer") {
    dependsOn("classes")
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("com.sdell.omega_edit.grpc.OmegaEditServerKt")
}

val omegaEditServerStartScripts = tasks.register<CreateStartScripts>("omegaEditServerStartScripts") {
    mainClass.set("com.sdell.omega_edit.grpc.OmegaEditServerKt")
    applicationName = "omega-edit-kt-grpc-server"
    outputDir = tasks.named<CreateStartScripts>("startScripts").get().outputDir
    classpath = tasks.named<CreateStartScripts>("startScripts").get().classpath
}

tasks.named("startScripts") {
    dependsOn(omegaEditServerStartScripts)
}

tasks.withType<Test> {
    useJUnit()

    testLogging {
        events = setOf(
            org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
        )
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showStandardStreams = true
    }
}
