#!/bin/bash

# Проверяем, существует ли файл .env
if [ -f .env ]; then
  echo "Loading environment variables from .env file..."

  # Экспортируем переменные из .env
  export $(grep -v '^#' .env | xargs)

  echo "Environment variables set successfully!"
else
  echo "Error: .env file not found."
  exit 1
fi