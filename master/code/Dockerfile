# Compile and package the application
RUN apt-get update && apt-get install -y maven

FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Run the application
FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/registration-login-spring-boot-security-thymeleaf-0.0.1-SNAPSHOT.jar Sapimentor_Demo_Project.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","Sapimentor_Demo_Project.jar"]

