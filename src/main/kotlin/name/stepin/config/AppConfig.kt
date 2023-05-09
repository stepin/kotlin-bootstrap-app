package name.stepin.config

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "app")
data class AppConfig(
    @JsonProperty("base-url")
    var baseUrl: String = "",
)
