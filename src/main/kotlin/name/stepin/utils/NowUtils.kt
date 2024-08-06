package name.stepin.utils

import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class NowUtils {
    fun nowLocalDate(): LocalDate = LocalDate.now()
}
