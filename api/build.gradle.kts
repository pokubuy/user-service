import com.google.protobuf.gradle.id

plugins {
    java
    alias(libs.plugins.protobuf)
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(libs.javax.annotation.api)
    compileOnly(libs.reactor.core)
    compileOnly(libs.reactor.grpc.stub)
    compileOnly(libs.grpc.all)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

protobuf {
    protoc {
        artifact = libs.protobuf.protoc.asProvider().get().toString()
    }

    plugins {
        id("grpc") {
            artifact = libs.protobuf.protoc.gen.grpc.get().toString()
        }
        id("reactor") {
            artifact = libs.reactor.grpc.asProvider().get().toString()
        }
    }

    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                id("grpc")
                id("reactor")
            }
        }
    }
}