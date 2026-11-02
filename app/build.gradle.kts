import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    java
    alias(libs.plugins.lombok)
    alias(libs.plugins.spring.boot)
}

repositories {
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

    implementation(libs.postgres)
    runtimeOnly(libs.flyway.postgres)
    implementation(libs.spring.boot.starter.flyway)

    implementation(libs.jackson.legacy.core)
    implementation(libs.jackson.legacy.databind)
    implementation(libs.jackson.legacy.dataformats.yaml)
    implementation(libs.resteasy.core)
    implementation(libs.keycloak.admin.client)
}

tasks.named<BootRun>("bootRun") {
    systemProperties["spring.profiles.active"] =
        if (project.hasProperty("prod")) "prod" else "dev"
}