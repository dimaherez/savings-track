# Stage 1: Build the project using a Java 21 JDK
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

# Copy the entire project into the container
COPY . .

# Make the wrapper executable
RUN chmod +x ./gradlew

# Build the fat jar
RUN ./gradlew buildFatJar --no-daemon

# Stage 2: Run the application on a lightweight Java 21 JRE
FROM eclipse-temurin:21-jre-alpine
EXPOSE 8080
RUN mkdir /app

# Copy the generated JAR from the build stage
COPY --from=build /app/build/libs/*-all.jar /app/ktor-app.jar

ENTRYPOINT ["java", "-jar", "/app/ktor-app.jar"]