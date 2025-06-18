FROM eclipse-temurin:21-jdk-alpine as builder

WORKDIR /app
COPY . .
RUN chmod +x ./gradlew && ./gradlew bootJar -x test

FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]