# Module Kotlin Bootstrap App

Пример документации на уровне приложения.

# Package name.stepin

Основной пакет этого приложения.

# Package name.stepin.config

Настройки приложения, Quarkus и библиотек.

# Package name.stepin.db.dao

SQL-код доступа к Postgres (jOOQ).

# Package name.stepin.db.entity

Entity-классы для репозиторных классов (Panache).

# Package name.stepin.db.repository

Panache-репозитории.

# Package name.stepin.db.sql

Автоматически сгенерированный маппинг схемы БД на Kotlin (jOOQ).
Из особенностей -- любые поля nullable.

# Package name.stepin.domain

Бизнес-логика, по возможности без технического кода.

# Package name.stepin.domain.quarkus.extensions.client

Трансляция клиентов на бизнес-язык (модели).

# Package name.stepin.domain.quarkus.extensions.model

Модели -- entity, value objects.

# Package name.stepin.domain.quarkus.extensions.service

Доменные сервисы и use cases.

# Package name.stepin.graphql

Входная точка для GraphQL. По сути такой же контроллер как и для HTTP.

# Package name.stepin.service

Сервисы уровня приложения (могут координировать несколько доменных сервисов).
Их суть -- протоколонезависимое API приложения.

# Package name.stepin.utils

Вспомогательные классы, которые в идеальном мире должны быть в других репозитариях.
Например, NowUtils позволяет писать unit-тесты, если нужно замокать текущее время.

# Package name.stepin.web

Входная точка для HTTP.
