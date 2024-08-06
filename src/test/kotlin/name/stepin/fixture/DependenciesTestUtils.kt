package name.stepin.fixture

object DependenciesTestUtils {
    const val BASE_PACKAGE = "name.stepin"

    val genericPackages =
        listOf(
            "kotlin..",
            "kotlinx..",
            "org.jetbrains.annotations..",
            "java..",
            "javax..",
            "org.springframework..",
            "reactor..",
            "org.apache.logging..",
        )
}
