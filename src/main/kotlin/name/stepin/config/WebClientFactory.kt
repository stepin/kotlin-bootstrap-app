package name.stepin.config

import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

object WebClientFactory {

    fun <S> createClient(baseUrl: String, serviceType: Class<S>): S {
        val client = webClient(baseUrl).build()
        val proxyFactory = HttpServiceProxyFactory
            .builder(WebClientAdapter.forClient(client))
            .build()
        return proxyFactory.createClient(serviceType)
    }

    private fun webClient(baseUrl: String): WebClient.Builder {
        return WebClient.builder()
            .baseUrl(baseUrl)
    }
}
