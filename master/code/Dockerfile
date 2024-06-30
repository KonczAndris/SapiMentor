## Ez az Onrender-hez kell:
## Compile and package the application
#FROM maven:3.8.5-openjdk-17 AS build
#COPY . .
#RUN mvn clean package -DskipTests -e -X
#
## Run the application
#FROM openjdk:17.0.1-jdk-slim
#COPY --from=build /target/registration-login-spring-boot-security-thymeleaf-0.0.1-SNAPSHOT.jar Sapimentor_Demo_Project.jar
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","Sapimentor_Demo_Project.jar"]


FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /code
COPY pom.xml .
COPY src ./src
RUN mvn clean install

FROM openjdk:17.0.1-jdk-slim
WORKDIR /code
COPY --from=build /code/target/registration-login-spring-boot-security-thymeleaf-0.0.1-SNAPSHOT.jar SapiMentor_AWS.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar","SapiMentor_AWS.jar"]

