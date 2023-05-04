package name.stepin.client.quarkus.extensions

import name.stepin.config.WebClientFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QuarkusExtensionsConfig {

    @Bean
    fun personService() = WebClientFactory.createClient(
        baseUrl = "https://stage.code.quarkus.io/api",
        serviceType = QuarkusExtensionsClient::class.java,
    )
}
