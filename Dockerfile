# --- Build Stage ---
FROM maven:3.8.4-jdk-11 AS build
WORKDIR /app

# Copy the whole project into the container
COPY . .

# Build the project (skipping tests for velocity)
RUN mvn -f JtProject/pom.xml clean package -DskipTests

# --- Runtime Stage ---
FROM eclipse-temurin:11-jre
WORKDIR /app

# Copy only the built JAR from the build stage
COPY --from=build /app/JtProject/target/marketplace-1.0.0.jar app.jar

# Dynamic Port Support for Cloud Platforms (Render/Railway/Heroku)
ENV PORT 8080
EXPOSE 8080

# Professional Startup Script
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]
