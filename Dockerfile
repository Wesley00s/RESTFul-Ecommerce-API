FROM gradle:8.6-jdk21-jammy AS build
WORKDIR /app

COPY build.gradle settings.gradle ./
COPY gradle.properties* ./
COPY gradle ./gradle

RUN gradle dependencies --no-daemon || \
    { echo "Ignoring possible download failures during dependency resolution"; true; }

COPY src ./src
RUN gradle build --no-daemon -x test

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]