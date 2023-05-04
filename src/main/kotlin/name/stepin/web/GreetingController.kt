package name.stepin.web

import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.constraints.Size
import name.stepin.service.HelloService
import name.stepin.web.model.HelloRemoteResponse
import name.stepin.web.model.TestArgumentsRequest
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
class GreetingController(
    private val helloService: HelloService,
) {
    companion object : Logging

    @GetMapping("/api/hello", produces = [MediaType.TEXT_PLAIN_VALUE])
    @Operation(summary = "Simple HTTP endpoint")
    suspend fun hello(
        @RequestParam
        @Size(min = 3)
        name: String,
    ) = "Hello RESTEasy $name, ${helloService.hello()}"

    @GetMapping("/api/helloRemote")
    @Operation(summary = "HTTP endpoint with remote call")
    suspend fun helloRemote(): HelloRemoteResponse {
        val message = "Hello RESTEasy ${helloService.helloRemote()}"

        return HelloRemoteResponse(message)
    }

    @PostMapping("/api/hello/{id}")
    suspend fun testArguments(
        @PathVariable("id") id: Long,
        @RequestHeader("my-header") header: String,
        @RequestParam("myQueryParam") queryParam: String,
        @RequestBody body: TestArgumentsRequest,
    ): HelloRemoteResponse {
        logger.info { "testArguments $id $header $queryParam $body" }

        return HelloRemoteResponse("message: $id $header $queryParam $body")
    }
}
