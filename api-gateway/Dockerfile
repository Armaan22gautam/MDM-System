FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY . .
# Tests are skipped because integration tests require running infrastructure (DB, Kafka, Redis)
RUN mvn clean package -pl api-gateway -am -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /workspace/api-gateway/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
