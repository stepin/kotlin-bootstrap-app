package name.stepin.utils

import jakarta.enterprise.context.ApplicationScoped
import java.time.LocalDate

@ApplicationScoped
class NowUtils {

    fun nowLocalDate(): LocalDate = LocalDate.now()
}
