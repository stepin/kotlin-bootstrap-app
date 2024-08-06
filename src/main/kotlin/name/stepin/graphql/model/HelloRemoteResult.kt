package name.stepin.graphql.model

data class HelloRemoteResult(
    val status: ResultStatus,
    val message: String,
)

enum class ResultStatus {
    SUCCESS,
    ERROR,
}
