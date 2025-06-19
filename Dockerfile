FROM eclipse-temurin:21.0.4_7-jdk

WORKDIR /app

COPY build/libs/E-Commerce-Application-0.0.1-SNAPSHOT.jar app.jar

COPY .env .

ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
