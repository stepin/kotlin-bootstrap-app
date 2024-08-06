package name.stepin.config

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.stereotype.Component

class MyLivenessCheckTest {
    @Test
    fun `MyLivenessCheck should have Component annotation`() {
        val clazz = MyLivenessCheck::class.java
        assertEquals(1, clazz.getAnnotationsByType(Component::class.java).size)
    }

    @Test
    fun `MyLivenessCheck should implement HealthIndicator`() {
        val clazz = MyLivenessCheck()
        assertNotNull(clazz as? HealthIndicator)
    }

    @Test
    fun `health main case`() {
        val expected = Health.up().status("alive").build()

        val actual = MyLivenessCheck().health()

        assertEquals(expected, actual)
    }
}
