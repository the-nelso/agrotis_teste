# Use a imagem base do OpenJDK 21
FROM maven:3.9.9-amazoncorretto-21 as maven-builder
COPY src /app/src
COPY pom.xml /app

RUN mvn -f /app/pom.xml clean package -DskipTests
FROM openjdk:21

COPY --from=maven-builder app/target/demo-0.0.1-SNAPSHOT.jar /app-service/app.jar
WORKDIR /app-service

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]