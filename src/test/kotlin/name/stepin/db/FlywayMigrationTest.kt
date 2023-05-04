package name.stepin.db

import name.stepin.fixture.PostgresFactory.dataSource
import name.stepin.fixture.PostgresFactory.postgres
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.lang.Thread.sleep

@Testcontainers
class FlywayMigrationTest {

    @Container
    var postgres = postgres()

    @Test
    fun `run flyway migrations`() {
        // waiting for Postgres to start (looks like default waiting strategy is buggy)
        sleep(2000)

        val flyway = Flyway.configure()
            .schemas("public")
            .dataSource(dataSource(postgres))
            .load()

        flyway.info()

        flyway.migrate()
    }
}
