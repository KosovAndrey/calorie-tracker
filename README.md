# Calorie Tracker

REST API для отслеживания потребления калорий и питательных веществ.

## Описание

Calorie Tracker - это веб-приложение, которое помогает пользователям отслеживать их ежедневное потребление калорий и питательных веществ. Приложение позволяет:

- Создавать и управлять профилями пользователей
- Добавлять и редактировать приемы пищи
- Управлять списком блюд
- Отслеживать потребление калорий, белков, жиров и углеводов
- Получать отчеты о питании за определенный период
- Устанавливать и контролировать дневные лимиты калорий

## Технологии

- Java 17
- Spring Boot 3.4.4
- Spring Data JPA
- PostgreSQL
- Liquibase
- Lombok
- Maven
- JUnit 5
- Mockito

## Требования

- JDK 17 или выше
- Maven 3.8.1 или выше
- PostgreSQL 15 или выше
- Postman (для тестирования API)

## Установка и запуск

1. Клонируйте репозиторий:
```bash
git clone https://github.com/KosovAndrey/calorie-tracker.git
```

2. Создайте базу данных PostgreSQL:
```sql
CREATE DATABASE calorietracker;
```

3. Создайте файл `.env` на основе `.env.example`:
```bash
cp .env.example .env
```

4. Настройте переменные окружения в файле `.env`:
```bash
# Database Configuration
HOST=localhost:5432
POSTGRES_DB=calorietracker
POSTGRES_USER=your_username
POSTGRES_PASSWORD=your_password
```

5. Соберите проект:
```bash
mvn clean install
```

6. Запустите приложение:
```bash
mvn spring-boot:run
```

## Тестирование API

Для тестирования API доступна коллекция Postman в папке `postman/`. Коллекция содержит все необходимые запросы для тестирования функциональности API:

- Импортируйте коллекцию в Postman
- Запустите тесты

## Конфигурация

Приложение использует следующие настройки по умолчанию:

- Порт сервера: 8080
- База данных: PostgreSQL
- Миграции: Liquibase
- Hibernate DDL: none (управление схемой через Liquibase)

Все настройки базы данных должны быть указаны в файле `.env`. Не забудьте добавить `.env` в `.gitignore` для безопасности.

## API Endpoints

### Пользователи
- `POST /api/v1/users` - Создание нового пользователя
- `GET /api/v1/users/{id}` - Получение информации о пользователе
- `PUT /api/v1/users/{id}` - Обновление информации о пользователе
- `DELETE /api/v1/users/{id}` - Удаление пользователя

### Блюда
- `POST /api/v1/dishes` - Создание нового блюда
- `GET /api/v1/dishes/{id}` - Получение информации о блюде
- `PUT /api/v1/dishes/{id}` - Обновление информации о блюде
- `DELETE /api/v1/dishes/{id}` - Удаление блюда
- `GET /api/v1/dishes/search?query={query}` - Поиск блюд по названию

### Приемы пищи
- `POST /api/v1/meals` - Создание нового приема пищи
- `GET /api/v1/meals/{id}` - Получение информации о приеме пищи
- `PUT /api/v1/meals/{id}` - Обновление информации о приеме пищи
- `DELETE /api/v1/meals/{id}` - Удаление приема пищи
- `POST /api/v1/meals/{id}/items` - Создание элемента приема пищи

### Отчеты
- `GET /api/v1/reports/daily?date={date}&userId={userId}` - Получение дневного отчета
- `GET /api/v1/reports/calorie-limit?date={date}&userId={userId}` - Проверка лимита калорий
- `GET /api/v1/reports/history?startDate={startDate}&endDate={endDate}&userId={userId}` - Получение истории питания

## Тестирование

Для запуска тестов используйте команду:
```bash
mvn test
```

## Лицензия

MIT License
