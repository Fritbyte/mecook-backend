# Me Cook API Server

Серверная часть проекта Me Cook разработана для обеспечения надежного и эффективного взаимодействия мобильного
приложения с базой данных рецептов. Она отвечает за обработку запросов от клиентов, управлением данными и
предоставлением необходимой информации в удобном формате.

## Содержание
- [Обзор проекта](#обзор-проекта)
- [Технологический стек](#технологический-стек)
- [Требования к системе](#требования-к-системе)
- [Инструкция по установке](#инструкция-по-установке)
- [Запуск и настройка](#запуск-и-настройка)
- [Архитектура проекта](#архитектура-проекта)
- [API документация](#api-документация)
  - [Аутентификация](#аутентификация)
  - [Страны](#страны)
  - [Ингредиенты](#ингредиенты)
  - [Рецепты](#рецепты)
  - [Избранное](#избранное)
- [Коды ошибок](#коды-ошибок)
- [Примеры использования](#примеры-использования)
- [Разработка и тестирование](#разработка-и-тестирование)
- [Лицензия](#лицензия)

## Обзор проекта

Серверная часть Me Cook предоставляет REST API для доступа к базе рецептов и управления пользовательскими данными. Основные функциональные возможности включают:

1. **Аутентификация и авторизация пользователей**: регистрация, вход, JWT-токены.
2. **Управление рецептами**: поиск, фильтрация, создание, обновление.
3. **Работа с избранным**: добавление/удаление рецептов из избранного для зарегистрированных пользователей.
4. **Фильтрация по странам и ингредиентам**: поиск рецептов на основе критериев.

### Основные компоненты серверной части:

1. **REST API**: обеспечивает интерфейс для взаимодействия мобильного приложения с сервером. С помощью HTTP-запросов
   пользователи могут получать данные о рецептах, сохранять их в избранное и делиться.
2. **Сервисный слой**: содержит бизнес-логику приложения. Обрабатывает запросы от контроллеров, взаимодействует с базой
   данных через DAO и возвращает результат обратно контроллерам для формирования ответов клиентам.
3. **Слой доступа к данным (DAO)**: отвечает за прямое взаимодействие с базой данных. Реализует операции CRUD (создание,
   чтение, обновление, удаление) для различных сущностей, таких как рецепты, пользователи и ингредиенты.
4. **База данных**: хранит всю необходимую информацию, включая данные о рецептах, пользователях и других связанных
   сущностях.

## Технологический стек

- **Java 23** - основной язык программирования для разработки серверной части.
- **Spring Boot 3.2.x** - фреймворк для создания REST API.
- **Spring Security** - для аутентификации и авторизации.
- **Spring Data JPA** - для работы с базой данных.
- **Maven** - система сборки и управления зависимостями.
- **MySQL 8.0** - для хранения данных.
- **JWT (JSON Web Token)** - для безопасной передачи информации пользователя.

## Требования к системе

Для успешной установки и запуска проекта необходимы:

- **JDK 21** или выше
- **Maven 3.8+**
- **MySQL 8.0** или выше
- Минимум 2 ГБ оперативной памяти
- Минимум 1 ГБ свободного места на диске
- Поддержка UTF-8 в системе и базе данных

## Инструкция по установке

### 1. Клонирование репозитория

```bash
git clone https://github.com/Fritbyte/mecook-backend.git
cd mecook-backend
```

### 2. Настройка конфигурации

Создайте файл `src/main/resources/application-dev.properties` со следующим содержимым:

```properties
# Подключение к базе данных
spring.datasource.url=jdbc:mysql://localhost:3306/mecook?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8
spring.datasource.username=mecook_user
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Hibernate настройки
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=true

# JWT настройки
jwt.secret=your_jwt_secret_key_which_should_be_at_least_32_characters_long
jwt.expiration=86400000

# Настройки сервера
server.port=8080
```

Замените `your_password` и `your_jwt_secret_key_which_should_be_at_least_32_characters_long` на ваши значения.

### 3. Сборка проекта

```bash
./mvnw clean install
```

## Запуск и настройка

### Запуск в режиме разработки

```bash
./mvnw spring-boot:run -Dspring.profiles.active=dev
```

### Запуск в production режиме

1. Создайте JAR-файл:
   ```bash
   ./mvnw clean package -P prod
   ```

2. Запустите приложение:
   ```bash
   java -jar target/mecook-backend-1.0.0.jar --spring.profiles.active=prod
   ```

### Проверка работоспособности

После запуска проверьте доступность сервера, обратившись к:

```
http://localhost:8080/api/ping
```

Должен вернуться статус 200 OK.

## Архитектура проекта

Проект построен по принципу разделения ответственности и использует архитектуру, основанную на слоях:

### Слои приложения:

- **Controller Layer**: REST контроллеры, которые принимают HTTP-запросы и возвращают ответы.
- **Service Layer**: Бизнес-логика приложения, разделенная на команды (commands) и запросы (queries).
- **Repository Layer**: Доступ к данным через Spring Data JPA.
- **Model Layer**: Сущности базы данных и объекты передачи данных (DTO).
- **Security Layer**: Аутентификация и авторизация.

### Структура проекта:

```
mecook-backend/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── mecook/
│       │           └── mecookbackend/
│       │               ├── MeCookApplication.java
│       │               ├── application/
│       │               │   ├── command/
│       │               │   ├── dto/
│       │               │   │   ├── input/
│       │               │   │   └── output/
│       │               │   └── query/
│       │               ├── config/
│       │               ├── domain/
│       │               │   ├── exception/
│       │               │   ├── model/
│       │               │   └── repository/ 
│       │               ├── infrastructure/
│       │               │   ├── persistence/
│       │               │   │   └── jpa/
│       │               │   └── security/
│       │               └── web/
│       │                   ├── controller/
│       │                   ├── dto/    
│       │                   └── exception/ 
│       └── resources/
│           ├── META-INF/
│           ├── application.properties
│           ├── application-dev.properties
│           ├── application-prod.properties
│           └── application-test.properties  
```

### Структура пакетов приложения:

#### Пакет application/command
Содержит сервисы, реализующие модификацию данных:
- `CountryCommandService.java` - управление странами
- `DishCommandService.java` - управление блюдами
- `UserAuthenticationService.java` - аутентификация пользователей
- `UserRegistrationService.java` - регистрация новых пользователей

#### Пакет domain/model
Основные сущности приложения:
- `Country.java` - информация о странах
- `Dish.java` - информация о блюдах
- `DishContentBlock.java` - блоки контента для описания блюд
- `Ingredient.java` - ингредиенты для блюд
- `User.java` - пользовательские данные

#### Пакет web/controller
REST API контроллеры:
- `AuthController.java` - авторизация пользователей
- `CountryController.java` - операции со странами
- `DishController.java` - операции с блюдами
- `IngredientController.java` - операции с ингредиентами
- `UserController.java` - операции с пользователями

## API документация

### Базовый URL

Все запросы к API должны начинаться с базового URL:

```
http://localhost:8080/api
```

### Аутентификация

#### Регистрация нового пользователя

- **URL**: `/auth/register`
- **Метод**: `POST`
- **Тело запроса**:
  ```json
  {
    "username": "user123",
    "email": "user@example.com",
    "password": "password123",
    "confirmPassword": "password123"
  }
  ```
- **Успешный ответ (200 OK)**:
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userId": 1,
    "username": "user123"
  }
  ```
- **Ошибка (400 Bad Request)**:
  ```json
  {
    "message": "Пользователь с таким именем или email уже существует",
    "statusCode": 400
  }
  ```

#### Вход в систему

- **URL**: `/auth/login`
- **Метод**: `POST`
- **Тело запроса**:
  ```json
  {
    "username": "user123",
    "password": "password123"
  }
  ```
- **Успешный ответ (200 OK)**:
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userId": 1,
    "username": "user123"
  }
  ```
- **Ошибка (401 Unauthorized)**:
  ```json
  {
    "message": "Неверное имя пользователя или пароль",
    "statusCode": 401
  }
  ```

### Страны

#### Получение всех стран

- **URL**: `/countries`
- **Метод**: `GET`
- **Аутентификация**: Не требуется
- **Успешный ответ (200 OK)**:
  ```json
  [
    {
      "id": 1,
      "name": "Италия",
      "imageUrl": "http://example.com/images/italy.jpg"
    },
    {
      "id": 2,
      "name": "Франция",
      "imageUrl": "http://example.com/images/france.jpg"
    }
  ]
  ```

#### Получение страны по ID

- **URL**: `/countries/{id}`
- **Метод**: `GET`
- **Аутентификация**: Не требуется
- **Успешный ответ (200 OK)**:
  ```json
  {
    "id": 1,
    "name": "Италия",
    "imageUrl": "http://example.com/images/italy.jpg"
  }
  ```
- **Ошибка (404 Not Found)**:
  ```json
  {
    "message": "Страна не найдена",
    "statusCode": 404
  }
  ```

### Ингредиенты

#### Получение всех ингредиентов

- **URL**: `/ingredients`
- **Метод**: `GET`
- **Аутентификация**: Не требуется
- **Успешный ответ (200 OK)**:
  ```json
  [
    {
      "id": 1,
      "name": "Томаты"
    },
    {
      "id": 2,
      "name": "Сыр"
    }
  ]
  ```

#### Получение ингредиента по ID

- **URL**: `/ingredients/{id}`
- **Метод**: `GET`
- **Аутентификация**: Не требуется
- **Успешный ответ (200 OK)**:
  ```json
  {
    "id": 1,
    "name": "Томаты"
  }
  ```
- **Ошибка (404 Not Found)**:
  ```json
  {
    "message": "Ингредиент не найден",
    "statusCode": 404
  }
  ```

### Рецепты

#### Получение всех рецептов

- **URL**: `/dishes`
- **Метод**: `GET`
- **Аутентификация**: Не требуется
- **Параметры запроса**:
  - `countryId` (опционально): ID страны для фильтрации
  - `ingredients` (опционально): список ID ингредиентов, разделенных запятыми
- **Успешный ответ (200 OK)**:
  ```json
  [
    {
      "id": 1,
      "name": "Пицца Маргарита",
      "description": "Классическая итальянская пицца",
      "country": "Италия",
      "ingredients": ["Тесто", "Томаты", "Сыр Моцарелла", "Базилик"]
    },
    {
      "id": 2,
      "name": "Лазанья",
      "description": "Традиционное итальянское блюдо",
      "country": "Италия",
      "ingredients": ["Лазанья", "Фарш", "Томаты", "Сыр"]
    }
  ]
  ```

#### Получение рекомендуемых рецептов

- **URL**: `/dishes/recommended`
- **Метод**: `GET`
- **Аутентификация**: Не требуется
- **Успешный ответ (200 OK)**:
  ```json
  [
    {
      "id": 3,
      "name": "Карбонара",
      "description": "Итальянская паста с беконом и сыром",
      "country": "Италия",
      "ingredients": ["Паста", "Бекон", "Сыр Пармезан", "Яйца"]
    }
  ]
  ```

#### Получение рецепта по ID

- **URL**: `/dishes/{id}`
- **Метод**: `GET`
- **Аутентификация**: Не требуется
- **Успешный ответ (200 OK)**:
  ```json
  {
    "id": 1,
    "name": "Пицца Маргарита",
    "description": "Классическая итальянская пицца",
    "country": "Италия",
    "ingredients": ["Тесто", "Томаты", "Сыр Моцарелла", "Базилик"],
    "steps": [
      {
        "stepNumber": 1,
        "description": "Раскатайте тесто",
        "imageUrl": "http://example.com/images/step1.jpg"
      },
      {
        "stepNumber": 2,
        "description": "Добавьте томатный соус",
        "imageUrl": "http://example.com/images/step2.jpg"
      }
    ]
  }
  ```
- **Ошибка (404 Not Found)**:
  ```json
  {
    "message": "Рецепт не найден",
    "statusCode": 404
  }
  ```

### Избранное

#### Получение избранных рецептов пользователя

- **URL**: `/favorites`
- **Метод**: `GET`
- **Аутентификация**: Требуется (JWT токен)
- **Заголовки**: 
  - `Authorization: Bearer {token}`
- **Успешный ответ (200 OK)**:
  ```json
  [
    {
      "id": 1,
      "name": "Пицца Маргарита",
      "description": "Классическая итальянская пицца",
      "country": "Италия",
      "ingredients": ["Тесто", "Томаты", "Сыр Моцарелла", "Базилик"]
    }
  ]
  ```

#### Добавление рецепта в избранное

- **URL**: `/favorites/add`
- **Метод**: `POST`
- **Аутентификация**: Требуется (JWT токен)
- **Заголовки**: 
  - `Authorization: Bearer {token}`
- **Тело запроса**:
  ```json
  {
    "dishId": 1
  }
  ```
- **Успешный ответ (200 OK)**:
  ```json
  {
    "message": "Рецепт успешно добавлен в избранное",
    "success": true
  }
  ```
- **Ошибка (404 Not Found)**:
  ```json
  {
    "message": "Рецепт не найден",
    "statusCode": 404
  }
  ```

#### Удаление рецепта из избранного

- **URL**: `/favorites/remove`
- **Метод**: `POST`
- **Аутентификация**: Требуется (JWT токен)
- **Заголовки**: 
  - `Authorization: Bearer {token}`
- **Тело запроса**:
  ```json
  {
    "dishId": 1
  }
  ```
- **Успешный ответ (200 OK)**:
  ```json
  {
    "message": "Рецепт успешно удален из избранного",
    "success": true
  }
  ```

## Коды ошибок

Сервер может возвращать следующие коды ошибок:

- **400 Bad Request** - неверный запрос или проблемы с валидацией
- **401 Unauthorized** - отсутствует или некорректный токен аутентификации
- **403 Forbidden** - недостаточно прав для выполнения операции
- **404 Not Found** - запрашиваемый ресурс не найден
- **500 Internal Server Error** - внутренняя ошибка сервера

## Примеры использования

### Поиск рецептов по ингредиентам

```bash
curl -X GET "http://localhost:8080/api/dishes?ingredients=1,2,3" -H "Content-Type: application/json"
```

### Авторизация и получение избранных рецептов

```bash
# Авторизация
TOKEN=$(curl -X POST "http://localhost:8080/api/auth/login" \
     -H "Content-Type: application/json" \
     -d '{"username":"user123","password":"password123"}' \
     | jq -r '.token')

# Получение избранных рецептов
curl -X GET "http://localhost:8080/api/favorites" \
     -H "Authorization: Bearer $TOKEN"
```

## Разработка и тестирование

### Запуск тестов

```bash
./mvnw test
```

### Сборка и запуск проекта с профилем тестирования

```bash
./mvnw clean install -P test
./mvnw spring-boot:run -Dspring.profiles.active=test
```

### Настройка окружения для разработки

1. Установите IntelliJ IDEA или Eclipse
2. Импортируйте проект как Maven-проект
3. Настройте Run Configuration с параметром `-Dspring.profiles.active=dev`

## Лицензия

Проект распространяется под лицензией MIT. Подробности см. в файле [LICENSE](LICENSE).