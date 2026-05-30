# ── Stage 1: Build ────────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -q
COPY src ./src
RUN mvn clean package -DskipTests -q

# ── Stage 2: Run ──────────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080

# -Xmx400m keeps memory under Render free tier 512MB limit
ENTRYPOINT ["java", "-Xmx400m", "-Xms256m", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-jar", "app.jar"]
