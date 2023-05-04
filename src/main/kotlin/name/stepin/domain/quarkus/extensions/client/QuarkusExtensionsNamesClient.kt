package name.stepin.domain.quarkus.extensions.client

import name.stepin.client.quarkus.extensions.QuarkusExtensionsClient
import name.stepin.domain.quarkus.extensions.model.ExtensionName
import org.springframework.stereotype.Service

@Service
class QuarkusExtensionsNamesClient(
    private val client: QuarkusExtensionsClient,
) {

    suspend fun getNames(): List<ExtensionName> {
        return client.getExtensionsById("io.quarkus:quarkus-resteasy-reactive")
            .map { ExtensionName(it.name) }
    }
}
