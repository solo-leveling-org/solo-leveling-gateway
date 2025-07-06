# Build stage
FROM maven:3.9.10-eclipse-temurin-24 AS build

# Устанавливаем Node.js (необходим для сборки Vaadin фронтенда)
RUN curl -fsSL https://deb.nodesource.com/setup_20.x  | bash - && \
    apt-get install -y nodejs

# Создаём пользователя для безопасности
RUN useradd -m myuser && \
    mkdir -p /usr/src/app && \
    chown myuser:myuser /usr/src/app

WORKDIR /usr/src/app
USER myuser

# Копируем исходники
COPY --chown=myuser:myuser . .

# Собираем проект
RUN mvn clean package -DskipTests -Pproduction --settings settings.xml

# Run stage
FROM openjdk:24

# Копируем собранный JAR и frontend
COPY --from=build /usr/src/app/solo-leveling-ui-service/target/*.jar /app/solo-leveling.jar
COPY --from=build /usr/src/app/solo-leveling-ui-service/src/main/frontend /app/frontend

# Безопасность: создаём пользователя
RUN useradd -m myuser && \
    mkdir -p /app && \
    chown myuser:myuser /app

USER myuser
WORKDIR /app

# Открываем порт
EXPOSE 8080

# Запускаем приложение
CMD ["java", "-jar", "solo-leveling-ui.jar"]