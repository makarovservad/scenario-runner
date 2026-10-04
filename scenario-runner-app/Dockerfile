FROM maven:3.9.11-eclipse-temurin-21 AS build
WORKDIR /src
COPY pom.xml .
COPY scenario-runner-app/pom.xml scenario-runner-app/pom.xml
COPY scenario-runner-app/src scenario-runner-app/src
RUN mvn -q -pl scenario-runner-app -am package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /src/scenario-runner-app/target/scenario-runner-app-0.1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
