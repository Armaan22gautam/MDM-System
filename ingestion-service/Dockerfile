FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY . .
# Tests are skipped because integration tests require running infrastructure (DB, Kafka, Redis)
RUN mvn clean package -pl ingestion-service -am -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /workspace/ingestion-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
