package name.stepin.graphql

import jakarta.validation.Valid
import jakarta.validation.constraints.Size
import name.stepin.graphql.model.HelloRemoteResult
import name.stepin.graphql.model.ResultStatus
import name.stepin.service.HelloService
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated
import reactor.core.publisher.Mono

@Controller
@Validated
class HelloGraphQL(
    private val helloService: HelloService,
) {
    companion object : Logging

    @QueryMapping
    @Valid
    suspend fun sayHello(
        @Argument
        @Size(min = 3)
        name: String,
    ): String {
        logger.info("sayHello $name")
        return "Hello GraphQL $name, ${helloService.hello()}"
    }

    @QueryMapping
    suspend fun helloRemote(): HelloRemoteResult {
        logger.info("helloRemote")
        return HelloRemoteResult(
            status = ResultStatus.SUCCESS,
            message = helloService.helloRemote(),
        )
    }

    @QueryMapping
    fun hello2(): Mono<String> {
        return Mono.just("hello2")
    }
}
