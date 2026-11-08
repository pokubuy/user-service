import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    java
    alias(libs.plugins.lombok)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.spring.boot)
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation(libs.spring.boot.starter.webflux)
    runtimeOnly(libs.postgres.r2dbc)
    implementation(libs.spring.boot.starter.data.r2dbc)

    runtimeOnly(libs.postgres)
    runtimeOnly(libs.flyway.postgres)
    implementation(libs.flyway.core)
    implementation(libs.spring.boot.starter.flyway)

    implementation(libs.keycloak.admin.client)

    implementation(libs.spring.grpc.server.starter)
    implementation(libs.reactor.grpc.stub)
    implementation(project(":api"))
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