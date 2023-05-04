package name.stepin.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "greeting")
data class GreetingConfig(
    var message: String = "",
)
