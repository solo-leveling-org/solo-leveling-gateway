FROM maven:3.9.8-eclipse-temurin-21 AS build

COPY src /app/src

COPY pom.xml /app

WORKDIR /app
RUN mvn clean install -U -DskipTests=true

FROM openjdk:21
COPY --from=build /app/target/solo-leveling-1.0.0.jar /app/solo-leveling.jar

WORKDIR /app

EXPOSE 8080

CMD ["java", "-jar", "solo-leveling.jar"]