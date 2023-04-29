package name.stepin.domain.quarkus.extensions.service

import jakarta.enterprise.context.ApplicationScoped
import name.stepin.domain.quarkus.extensions.client.QuarkusExtensionsNamesClient
import name.stepin.domain.quarkus.extensions.model.ExtensionName

@ApplicationScoped
class TopQuarkusExtensionsService(
    private val client: QuarkusExtensionsNamesClient
) {

    suspend fun getTopExtension(): ExtensionName? {
        return client.getNames().firstOrNull()
    }
}
