FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY user-service/.mvn user-service/.mvn
COPY user-service/mvnw user-service/pom.xml user-service/

WORKDIR /workspace/user-service
RUN chmod +x mvnw
RUN ./mvnw -B -DskipTests dependency:go-offline

COPY user-service/src src
RUN ./mvnw -B clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /workspace/user-service/target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
