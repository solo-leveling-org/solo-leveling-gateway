FROM maven:3.9.8-eclipse-temurin-21 AS build

RUN curl -fsSL https://deb.nodesource.com/setup_22.x | bash - && \
    apt-get install -y nodejs

COPY . /app
WORKDIR /app
RUN mvn clean install -U -DskipTests=true

FROM openjdk:21

COPY --from=build /app/target/*.jar /app/solo-leveling.jar

WORKDIR /app

EXPOSE 8080

CMD ["java", "-jar", "solo-leveling.jar"]