package name.stepin.utils

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDate

class NowUtilsTest {

    @Test
    fun `nowLocalDate return local time in UTC`() {
        val now = LocalDate.now()

        val actual = NowUtils().nowLocalDate()

        val duration = Duration.between(now.atTime(0, 0), actual.atTime(0, 0))
        assertTrue(duration.abs().seconds < 1)
    }
}
