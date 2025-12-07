# Use latest official Maven + JDK image to build
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set workdir
WORKDIR /app

# Copy everything into container
COPY . .

# Build the project (creates runnable JAR)
RUN mvn -q -e -DskipTests package

# Run stage: use lightweight JDK image
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/simpledata-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar

# Set Render PORT
ENV PORT=10000

# Expose port
EXPOSE 10000

# Run the app
CMD ["java", "-jar", "app.jar"]
