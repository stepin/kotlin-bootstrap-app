package name.stepin.config

import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.stereotype.Component

@Component
class MyLivenessCheck : HealthIndicator {
    override fun health(): Health {
        return Health.up().status("alive").build()
    }
}
