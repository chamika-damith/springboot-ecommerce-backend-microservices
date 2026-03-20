FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY product-service/.mvn product-service/.mvn
COPY product-service/mvnw product-service/pom.xml product-service/

WORKDIR /workspace/product-service
RUN chmod +x mvnw
RUN ./mvnw -B -DskipTests dependency:go-offline

COPY product-service/src src
RUN ./mvnw -B clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /workspace/product-service/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
