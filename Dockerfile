# ===================================================================================
# Stage 1: The Build Stage
# ... (this part remains unchanged)
# ===================================================================================
FROM maven:3.8-openjdk-8 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests

# ===================================================================================
# Stage 2: The Final Runtime Stage
# ===================================================================================
FROM openjdk:8-jre-slim
WORKDIR /app

# Create a non-root user and group for security purposes.
RUN groupadd -r spring && useradd -r -g spring appuser

# --- THIS IS THE FIX ---
# Change the ownership of the /app directory to our new user.
# This must be done BEFORE we switch to the appuser.
# It gives our application permission to write files (like the H2 database)
# inside its working directory.
RUN chown -R appuser:spring /app

# Switch to the non-root user
USER appuser

# Copy the built JAR file from the 'builder' stage.
COPY --from=builder /app/target/*.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# The command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]