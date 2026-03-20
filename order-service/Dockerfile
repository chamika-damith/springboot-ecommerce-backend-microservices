FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY product-service/.mvn product-service/.mvn
COPY product-service/mvnw product-service/pom.xml product-service/
COPY order-service/.mvn order-service/.mvn
COPY order-service/mvnw order-service/pom.xml order-service/

RUN chmod +x product-service/mvnw order-service/mvnw

WORKDIR /workspace/product-service
RUN ./mvnw -B -DskipTests dependency:go-offline
COPY product-service/src src
RUN ./mvnw -B clean install -DskipTests

WORKDIR /workspace/order-service
RUN ./mvnw -B -DskipTests dependency:go-offline
COPY order-service/src src
RUN ./mvnw -B clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /workspace/order-service/target/*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
