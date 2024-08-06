package name.stepin.config

import com.zaxxer.hikari.HikariDataSource
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class JooqConfig {
    @Primary
    @Bean
    fun dslContext(r2dbcConfig: R2dbcConfig) = DSL.using(r2dbcConfig.connectionFactory(), SQLDialect.POSTGRES)

    @Bean("jdbcDb")
    fun jdbcDslContext(dataSource: HikariDataSource): DSLContext {
        return DSL.using(dataSource.connection, SQLDialect.POSTGRES)
    }
}
