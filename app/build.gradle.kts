plugins {
    alias(libs.plugins.lombok)
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.logback.classic)
    implementation(libs.jackson.core)
    implementation(libs.spring.boot.starter.webflux)
    implementation(libs.spring.boot.starter.data.r2dbc)
    implementation(libs.spring.boot.starter.flyway)
    implementation(libs.jackson.legacy.core)
    implementation(libs.jackson.legacy.databind)
    implementation(libs.jackson.legacy.dataformats.yaml)
    implementation(libs.resteasy.core)
    implementation(libs.keycloak.admin.client)
}

application {
    mainClass = "org.pokubuy.userservice.Application"
}