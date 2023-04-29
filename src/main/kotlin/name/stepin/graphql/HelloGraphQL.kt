package name.stepin.graphql

import jakarta.validation.Valid
import jakarta.validation.constraints.Size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import name.stepin.domain.quarkus.extensions.service.TopQuarkusExtensionsService
import org.apache.logging.log4j.kotlin.Logging
import org.eclipse.microprofile.graphql.DefaultValue
import org.eclipse.microprofile.graphql.Description
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Query

@GraphQLApi
class HelloGraphQL(
    private val topQuarkusExtensionsService: TopQuarkusExtensionsService
) {
    companion object : Logging

    @Query
    @Description("Say hello")
    @Valid
    fun sayHello(
        @DefaultValue("World")
        @Size(min = 3)
        name: String
    ): String {
        logger.info("sayHello $name")
        return "Hello GraphQL $name"
    }

    // NOTE: no support for suspend
    // https://github.com/smallrye/smallrye-graphql/issues/1024
    @Query
    fun topQuarkusExtension(): String = runBlocking {
        withContext(Dispatchers.IO) {
            logger.info("topQuarkusExtension")
            return@withContext topQuarkusExtensionsService.getTopExtension()?.name ?: "none"
        }
    }
}
