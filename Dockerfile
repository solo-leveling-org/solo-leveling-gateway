# Build stage
FROM maven:3.9.10-amazoncorretto-24-alpine AS build

# Устанавливаем Node.js (необходим для сборки Vaadin фронтенда)
RUN apk add --no-cache nodejs npm

# Создаём пользователя для безопасности
RUN adduser -D myuser && \
    mkdir -p /usr/src/app && \
    chown myuser:myuser /usr/src/app

WORKDIR /usr/src/app
USER myuser

# Копируем исходники
COPY --chown=myuser:myuser . .

# Собираем проект
RUN mvn clean package -DskipTests -Pproduction --settings settings.xml

# Run stage
FROM amazoncorretto:24-alpine3.21-jdk

# Копируем собранный JAR и frontend
COPY --from=build /usr/src/app/solo-leveling-gateway-service/target/*.jar /app/solo-leveling-gateway.jar

# Безопасность: создаём пользователя
RUN adduser -D myuser && \
    mkdir -p /app && \
    chown myuser:myuser /app

USER myuser
WORKDIR /app

# Открываем порт
EXPOSE 8080

# Запускаем приложение
CMD ["java", \
    "--enable-native-access=ALL-UNNAMED", \
    "--add-opens", "java.base/java.lang=ALL-UNNAMED", \
     "-Dspring.profiles.active=prod", \
    "-jar", "solo-leveling-gateway.jar"]