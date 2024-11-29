FROM eclipse-temurin:21.0.4_7-jdk

WORKDIR /app

COPY build/libs/E-Commerce-Application-0.0.1-SNAPSHOT.jar app.jar
COPY .env /app/.env
ENTRYPOINT ["/bin/bash", "-c", "source /app/.env && java -jar app.jar"]
ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
