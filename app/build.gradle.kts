import com.google.protobuf.gradle.id
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    java
    alias(libs.plugins.lombok)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.protobuf)
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation(libs.logback.classic)
    implementation(libs.jackson.core)
    implementation(libs.spring.boot.starter.webflux)

    runtimeOnly(libs.scram.common)
    runtimeOnly(libs.scram.client)
    runtimeOnly(libs.postgres.r2dbc)
    implementation(libs.spring.boot.starter.data.r2dbc)

    runtimeOnly(libs.postgres)
    runtimeOnly(libs.flyway.postgres)
    implementation(libs.spring.boot.starter.flyway)

    implementation(libs.jackson.legacy.core)
    implementation(libs.jackson.legacy.databind)
    implementation(libs.jackson.legacy.dataformats.yaml)
    implementation(libs.keycloak.admin.client)

    implementation(libs.spring.boot.starter.grpc.server)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

tasks.named<BootRun>("bootRun") {
    systemProperties["spring.profiles.active"] =
        if (project.hasProperty("prod")) "prod" else "dev"
}

protobuf {
    plugins {
        id("grpc")
    }
}