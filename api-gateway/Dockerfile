FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY api-gateway/.mvn api-gateway/.mvn
COPY api-gateway/mvnw api-gateway/pom.xml api-gateway/

WORKDIR /workspace/api-gateway
RUN chmod +x mvnw
RUN ./mvnw -B -DskipTests dependency:go-offline

COPY api-gateway/src src
RUN ./mvnw -B clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /workspace/api-gateway/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
