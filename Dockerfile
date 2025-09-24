# Dockerfile

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy JAR (built locally or in CI)
COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]