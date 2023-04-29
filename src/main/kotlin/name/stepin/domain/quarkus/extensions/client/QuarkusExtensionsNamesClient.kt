package name.stepin.domain.quarkus.extensions.client

import jakarta.enterprise.context.ApplicationScoped
import name.stepin.client.test.QuarkusExtensionsClient
import name.stepin.domain.quarkus.extensions.model.ExtensionName
import org.eclipse.microprofile.rest.client.inject.RestClient

@ApplicationScoped
class QuarkusExtensionsNamesClient(
    @RestClient private val client: QuarkusExtensionsClient
) {

    suspend fun getNames(): List<ExtensionName> {
        return client.getExtensionsById("io.quarkus:quarkus-resteasy-reactive")
            .map { ExtensionName(it.name) }
    }
}
