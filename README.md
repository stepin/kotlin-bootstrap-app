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

Обычно в этом разделе кратко описывается само приложение и его взаимосвязи.

## Tech stack

- Kotlin 1.8
- Spring Boot 3 (reactive with Kotlin co-routines)
- Spring Data Repositories & jOOQ
- JUnit 5 with mockk
- Java 17
- Postgres
- Docker

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
./bin/start-postgres.sh
./bin/run-dev.sh
```

## Packaging and running the application

The application can be packaged using:

```shell script
./bin/build-docker.sh
```
