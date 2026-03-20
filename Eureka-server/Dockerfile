FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY Eureka-server/.mvn Eureka-server/.mvn
COPY Eureka-server/mvnw Eureka-server/pom.xml Eureka-server/

WORKDIR /workspace/Eureka-server
RUN chmod +x mvnw
RUN ./mvnw -B -DskipTests dependency:go-offline

COPY Eureka-server/src src
RUN ./mvnw -B clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /workspace/Eureka-server/target/*.jar app.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
