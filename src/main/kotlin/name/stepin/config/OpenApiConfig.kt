package name.stepin.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun openapi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Kotlin Bootstrap App API")
                    .description(
                        """
                            Some long description.
                        """.trimIndent(),
                    ),
            )
            .addServersItem(Server().url("http://localhost:8080/").description("local"))
            .addServersItem(Server().url("https://stage/").description("stage"))
            .addServersItem(Server().url("https://prod/").description("prod"))
    }
}
