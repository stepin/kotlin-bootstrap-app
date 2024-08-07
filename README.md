# kotlin-bootstrap-app

Это стартовый шаблон для новых приложений на Котлин.

Необходимость шаблона сверх https://start.spring.io вызвана следующими доработками:

1. Выбор технологий. Да, всегда пытаешься выбрать как-то осознанно,
но, в целом, многие варианты подойдут. Тем не менее нужно что-то выбрать. Как из набора start.spring.io,
так и дополнительно. Тут технологии как базовые, так и для улучшения опыта разработки (вспомогательные).
2. Необходимо связать все компоненты вместе. Именно этому и посвящен Spring Boot и start.spring.io. Тем не менее,
из кода будет видно, что этого недостаточно и нужно еще писать свои связки.
3. Лучшие значения по умолчанию: например, непонятно в какой вселенной по умолчанию нужно моментально завершаться,
а не постепенно завершать обработку запросов. Это одно из самых ярких, но параметров довольно много изменено.
4. Примеры: хорошо сразу видеть базовые примеры использования технологий. Можно практически сразу из реального
приложения их удалить, тем не менее они останутся в репозитарии шаблона. Это набор лучших практик показывает
как обычно нужно кодировать тот или иной случай.

Разницу между https://start.spring.io и тем, что получилось после ручной доработки можно легко проверить
по истории комитов.

Пофайловое описание шаблона есть в этом видео: https://www.youtube.com/watch?v=DF6aJMccSo0

На базе этого шаблона сделан еще один -- Шаблон event sourcing приложения: https://github.com/stepin/kotlin-event-sourcing-app

Обычно в этом разделе кратко описывается само приложение и его взаимосвязи.

## Tech stack

- Kotlin 1.9
- Spring Boot 3 (reactive with Kotlin co-routines)
- Spring Data Repositories & jOOQ
- JUnit 5 with mockk
- Java 21
- Postgres
- Docker
- KBRE

## Dev links

- App: http://localhost:8080/
- Dev UI: http://localhost:8081/actuator
- Swagger spec json: http://localhost:8080/v3/api-docs
- Swagger spec yaml: http://localhost:8080/v3/api-docs.yaml
- Swagger UI: http://localhost:8080/swagger-ui.html
- GraphQL endpoint: http://localhost:8080/graphql/
- GraphQL schema: http://localhost:8080/graphql/schema.graphql
- GraphQL UI: http://localhost:8080/graphiql
- Health liveness: http://localhost:8081/actuator/health/liveness
- Health readiness: http://localhost:8081/actuator/health/readiness
- Generic metrics: http://localhost:8081/actuator/metrics/disk.free
- Prometheus metrics: http://localhost:8081/actuator/prometheus
- Config props: http://localhost:8081/actuator/configprops
- Env variables: http://localhost:8081/actuator/env
- Log settings: http://localhost:8081/actuator/loggers
- DB migrations info: http://localhost:8081/actuator/flyway

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./bin/start-postgres
./bin/generate-flyway
./bin/generate-jooq
./bin/run-dev
```

## Packaging and running the application

The application can be packaged using:

```shell script
./bin/build-docker
```

## TODO

### Настройка/интеграция

1. Интеграция с https://uptrace.dev . Нужны статистики запросов, длительности с учетом БД, детали ошибок для простых
случаев (когда нет бюджетов на полноценное решение).

2. В prod env логирование должно быть в JSON, в остальных остаться как есть. Это не сложно и делается, просто нужно
будет как-нибудь настроить. Приоритет пока что низкий, т.к. сейчас не надо.

### Доработки ПО

В этом разделе о проблемах, которые вряд ли решаются простой настройкой -- скорее всего нужно либо ждать когда проект
доделают, либо самому доделывать.

Spring Data:

1. Как-то генерировать/синхронизировать/обновлять Spring Data Entity-классы из схемы БД. Idea и JPA Buddy
не видят JPA и не работают. А так приходится вручную обновлять entity и вручную писать при создании новых таблиц.

jOOQ:

1. Генератор рекордов должен учитывать nullability полей.
2. Полноценная поддержка Kotlin coroutines (без явных конвертеров из reactor). Не слишком критично, но все же можно
было бы адаптеры для Котлина сразу лучше написать.
3. Автоконфигурирование R2DBC. Сейчас нужно вручную.
4. Gradle-плагин должен уметь работать через R2DBC, чтобы не настраивать 2 data source.
5. Отдельные maven-артифакты для асинхронности, чтобы в API вообще не было ничего синхронного, а не @Blocked аннотации.

Spring Boot GraphQL:

1. Поддержка Code First аналогично OpenAPI: пишется код + аннотации, выдается схема в эндпоинт и для фреймворка,
а то писать тоже самое второй раз (схему после кода) как-то неэффективно.

В остальном никаких пожеланий по доработкам нет.

## Частые вопросы

### Почему не Ktor?

Ktor заменяет уровень Spring. Как видно из предыдущего раздела, у меня к нему вообще нет вопросов: оно просто работает,
а если что-то нужно донастроить, то легко ищется как, и все ситуации уже известны или гуглятся.

Смотрим сайт Ktor. Там 3 довода за него:

1. Kotlin and Coroutines -- Spring полноценно это поддерживает.
2. Lightweight and Flexible -- может быть аргумент, но нужно проверять.
3. Built and backed by JetBrains -- вообще не аргумент. Видимо, реальные аргументы закончились.

Тогда давайте подробнее про "Lightweight and Flexible":

1. Насколько быстрее будет стартовать в режиме разработки?
2. Насколько быстрее будет стартовать в продуктивном режиме (jvm и нативные сборки)?
3. Насколько меньше памяти будет использовать в продуктивном режиме (jvm и нативные сборки)?
4. Как в итоге это приложение будет выглядеть по чистоте кода и зависимостям -- что еще нужно будет добавить/заменить,
чтобы сохранить полный функционал (prometheus, health, graphql, db access, ...)?

Было бы интересно, если бы кто-то хорошо знакомый с Ktor и его окружением сделали версию этого приложения на Ktor. Тогда
можно было бы посравнивать и понять какой подход на данный момент лучше.

Начать можно с ветки ktor-initial, например. Первые ощущения -- им не хватает людей: примеры кода используют deprecated
методы (static), устаревшие плагины говорят, что они устарели(jib-gradle-plugin). Но это еще ни о чем не говорит,
может дальше будет лучше.
