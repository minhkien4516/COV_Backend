FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy source code
COPY . .

# Fix mvnw permission (if needed)
RUN chmod +x ./mvnw

# Build JAR
RUN ./mvnw clean package -DskipTests

# Copy built JAR
RUN cp target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]