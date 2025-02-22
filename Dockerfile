FROM maven:3.9.8-eclipse-temurin-21 AS build

COPY . /app

WORKDIR /app

RUN mvn clean install -U -DskipTests=true

FROM openjdk:21

COPY --from=build /app/target/solo-leveling-1.0.0.jar /app/solo-leveling.jar

COPY --from=build /app/frontend /app/src/main/frontend

WORKDIR /app

EXPOSE 8080

CMD ["java", "-jar", "solo-leveling.jar"]