package name.stepin.config

import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@Configuration
@EnableR2dbcRepositories
@EnableConfigurationProperties(R2dbcProperties::class)
class R2dbcConfig(
    private val r2dbcProperties: R2dbcProperties,
) : AbstractR2dbcConfiguration() {
    override fun connectionFactory(): ConnectionFactory {
        return ConnectionFactories.get(
            ConnectionFactoryOptions
                .parse(r2dbcProperties.url)
                .mutate()
                .option(ConnectionFactoryOptions.USER, r2dbcProperties.username)
                .option(ConnectionFactoryOptions.PASSWORD, r2dbcProperties.password)
                .build(),
        )
    }
}
