# Build stage
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Copy gradle wrapper and make it executable
COPY gradlew gradlew
COPY gradle gradle
RUN chmod +x gradlew

# Copy build files
COPY build.gradle settings.gradle ./

# Copy source code
COPY src src

# Build the application, skipping tests for faster build
RUN ./gradlew build -x test

# Runtime stage
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the built JAR from build stage
COPY --from=build /app/build/libs/*.jar ./

# Expose port 8080
EXPOSE 8080

# CORS Configuration via environment variables
# Uncomment and modify as needed:
# ENV CORS_ALLOWED_ORIGINS=https://your-frontend-domain.com
# ENV CORS_ALLOWED_METHODS=GET,POST,PUT,DELETE,PATCH,OPTIONS
# ENV CORS_ALLOWED_HEADERS=*
# ENV CORS_MAX_AGE=3600

# Run the application
CMD ["sh", "-c", "java -jar recruiting-microservices-0.0.1-SNAPSHOT.jar"]