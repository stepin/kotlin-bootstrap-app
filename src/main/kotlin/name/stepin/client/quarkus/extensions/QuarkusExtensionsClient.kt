package name.stepin.client.quarkus.extensions

import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange

interface QuarkusExtensionsClient {
    @GetExchange("/extensions")
    suspend fun getExtensionsById(
        @RequestParam("id") id: String,
    ): Set<Extension>

    data class Extension(
        val id: String,
        val name: String,
        val shortName: String,
        val keywords: List<String>,
    )
}
