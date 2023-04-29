package name.stepin.service

import jakarta.enterprise.context.ApplicationScoped
import name.stepin.client.test.QuarkusExtensionsClient
import name.stepin.db.dao.UsersDao
import org.apache.logging.log4j.kotlin.Logging
import org.eclipse.microprofile.rest.client.inject.RestClient

@ApplicationScoped
class HelloService(
    private val usersDao: UsersDao,
    @RestClient private val quarkusExtensionsClient: QuarkusExtensionsClient
) {
    companion object : Logging

    suspend fun hello(): String {
        logger.info { "hello" }

        val hello = usersDao.getAll().joinToString(", ") { it.email ?: "" }
        logger.debug { "hello1 result $hello" }
        return hello
    }

    suspend fun helloRemote(): String {
        logger.info { "helloRemote" }

        val hello =
            quarkusExtensionsClient.getExtensionsById("io.quarkus:quarkus-resteasy-reactive")
                .joinToString(", ") { it.name }
        logger.debug { "helloRemote result $hello" }
        return hello
    }
}
