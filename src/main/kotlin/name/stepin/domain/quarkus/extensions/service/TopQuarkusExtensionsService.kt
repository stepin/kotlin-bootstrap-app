package name.stepin.domain.quarkus.extensions.service

import name.stepin.domain.quarkus.extensions.client.QuarkusExtensionsNamesClient
import name.stepin.domain.quarkus.extensions.model.ExtensionName
import org.springframework.stereotype.Service

@Service
class TopQuarkusExtensionsService(
    private val client: QuarkusExtensionsNamesClient,
) {

    suspend fun getTopExtension(): ExtensionName? {
        return client.getNames().firstOrNull()
    }
}
