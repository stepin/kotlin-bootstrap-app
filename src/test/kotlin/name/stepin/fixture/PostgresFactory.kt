package name.stepin.fixture

import org.flywaydb.core.Flyway
import org.jooq.impl.DSL
import org.postgresql.ds.PGSimpleDataSource
import org.testcontainers.containers.PostgreSQLContainer

object PostgresFactory {
    fun postgres(): PostgreSQLContainer<*> =
        PostgreSQLContainer("postgres:15.2")
            .withReuse(true)

    fun dslContext(postgres: PostgreSQLContainer<*>) = DSL.using(postgres.createConnection(""))

    fun dataSource(postgres: PostgreSQLContainer<*>): PGSimpleDataSource =
        PGSimpleDataSource().apply {
            setUrl(postgres.jdbcUrl)
            user = postgres.username
            password = postgres.password
        }

    fun initDb(postgres: PostgreSQLContainer<*>) {
        Thread.sleep(2000)

        val flyway =
            Flyway.configure()
                .schemas("public")
                .dataSource(dataSource(postgres))
                .load()

        flyway.migrate()
    }
}
