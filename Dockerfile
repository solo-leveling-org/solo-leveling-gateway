# Build stage
FROM maven:3.9.8-eclipse-temurin-21 AS build

# Устанавливаем Node.js (необходим для сборки Vaadin фронтенда)
RUN curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y nodejs

# Создаем пользователя для безопасности
RUN useradd -m myuser
WORKDIR /usr/src/app
RUN chown myuser:myuser /usr/src/app
USER myuser

# Копируем pom.xml и устанавливаем зависимости
COPY --chown=myuser pom.xml ./
RUN mvn dependency:go-offline -Pproduction

# Копируем исходный код и фронтенд
COPY --chown=myuser:myuser src src
COPY --chown=myuser:myuser frontend frontend
COPY --chown=myuser package.json package-lock.json vite.config.ts ./

# Собираем приложение
RUN mvn clean package -DskipTests -Pproduction

# Run stage
FROM openjdk:21

# Копируем собранный JAR-файл
COPY --from=build /usr/src/app/target/*.jar /app/solo-leveling.jar
COPY --from=build /usr/src/app/frontend /app/frontend

# Создаем пользователя для безопасности
RUN useradd -m myuser
USER myuser

# Устанавливаем рабочую директорию
WORKDIR /app

# Открываем порт
EXPOSE 8080

# Запускаем приложение
CMD ["java", "-jar", "solo-leveling.jar"]