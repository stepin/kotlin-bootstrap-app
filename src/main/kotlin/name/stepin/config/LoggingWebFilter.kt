package name.stepin.config

import org.apache.logging.log4j.kotlin.Logging
import org.reactivestreams.Publisher
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpRequestDecorator
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.http.server.reactive.ServerHttpResponseDecorator
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.ServerWebExchangeDecorator
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

// Based on https://github.com/Baeldung/kotlin-tutorials/tree/master/spring-reactive-kotlin
@Component
class LoggingWebFilter : WebFilter {
    companion object : Logging

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val startMillis = System.currentTimeMillis()

        return chain.filter(LoggingWebExchange(startMillis, exchange)).doOnSuccess {
            val elapsed = System.currentTimeMillis() - startMillis
            logger.trace { "total elapsed: ${elapsed}ms" }
        }
    }
}

class LoggingWebExchange(
    startMillis: Long,
    delegate: ServerWebExchange,
) : ServerWebExchangeDecorator(delegate) {
    private val requestDecorator = LoggingRequestDecorator(delegate.request)
    private val responseDecorator = LoggingResponseDecorator(startMillis, delegate.response)

    override fun getRequest() = requestDecorator
    override fun getResponse() = responseDecorator
}

class LoggingRequestDecorator(delegate: ServerHttpRequest) : ServerHttpRequestDecorator(delegate) {
    companion object : Logging

    init {
        logger.debug {
            val method = delegate.method.name()

            val path = delegate.uri.path
            val query: String? = delegate.uri.query
            val queryWithQuestionMark = if (query.isNullOrBlank()) "" else "?$query"

            val headers = delegate.headers.asString()

            "$method $path$queryWithQuestionMark\n$headers"
        }
    }
}

class LoggingResponseDecorator(
    private val startMillis: Long,
    delegate: ServerHttpResponse,
) : ServerHttpResponseDecorator(delegate) {
    companion object : Logging

    override fun writeWith(body: Publisher<out DataBuffer>): Mono<Void> {
        return super.writeWith(
            Flux.from(body)
                .doOnNext {
                    logger.debug {
                        val code = delegate.statusCode
                        val elapsed = System.currentTimeMillis() - startMillis
                        "response: ${elapsed}ms $code"
                    }
                },
        )
    }
}

fun HttpHeaders.asString() = entries
    .joinToString("\n") { pair ->
        " ${pair.key}: [${pair.value.joinToString(";")}]"
    }
