# Stage 1: Build the project using a Java 21 JDK
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

# Copy the entire monorepo (root, shared, composeApp, server) into the container
COPY . .

# Make the wrapper executable
RUN chmod +x ./gradlew

# Tell the root wrapper to specifically build the server module
RUN ./gradlew :server:buildFatJar --no-daemon

# Stage 2: Run the application on a lightweight Java 21 JRE
FROM eclipse-temurin:21-jre-alpine
EXPOSE 8080
RUN mkdir /app

# Copy the generated JAR from the server module's specific build directory
COPY --from=build /app/server/build/libs/*-all.jar /app/ktor-app.jar

ENTRYPOINT ["java", "-jar", "/app/ktor-app.jar"]