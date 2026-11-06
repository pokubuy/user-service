FROM eclipse-temurin:25-jdk-jammy AS builder
WORKDIR /app

COPY app/ ./app/
COPY gradlew ./
COPY gradle/ ./gradle/
COPY settings.gradle.kts ./
COPY gradle.properties ./

RUN ./gradlew :app:bootJar --no-daemon

FROM eclipse-temurin:25-jre-alpine
WORKDIR app
COPY --from=builder /app/app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]