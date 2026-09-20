# =========================
# Build stage
# =========================
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /build

# Root parent POM
COPY pom.xml .

# Eureka module
COPY eureka-server/pom.xml eureka-server/pom.xml
COPY eureka-server/src eureka-server/src

# Build ONLY Eureka
RUN mvn -f eureka-server/pom.xml clean package -DskipTests


# =========================
# Runtime stage
# =========================
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /build/eureka-server/target/*.jar app.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "app.jar"]