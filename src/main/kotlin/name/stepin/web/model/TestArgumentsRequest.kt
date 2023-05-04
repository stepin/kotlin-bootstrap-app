package name.stepin.web.model

import com.fasterxml.jackson.annotation.JsonProperty

data class TestArgumentsRequest(
    @JsonProperty("some-thing")
    val someThing: String,
)
