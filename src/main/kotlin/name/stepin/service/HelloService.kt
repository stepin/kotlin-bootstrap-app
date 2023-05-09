package name.stepin.service

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.fold
import name.stepin.client.quarkus.extensions.QuarkusExtensionsClient
import name.stepin.config.AppConfig
import name.stepin.db.dao.UsersDao
import name.stepin.db.repository.UserRepository
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.stereotype.Service

@Service
class HelloService(
    private val usersDao: UsersDao,
    private val quarkusExtensionsClient: QuarkusExtensionsClient,
    private val appConfig: AppConfig,
    private val userRepository: UserRepository,
) {
    companion object : Logging

    suspend fun hello(): String = coroutineScope {
        logger.info { "hello" }
        val dao1 = async { usersDao.byId(2) }
        val dao2 = async { usersDao.byIdNullable(2) }
        val dao3 = async { usersDao.newRecord().apply { email = "my3@example.com" } }
        val dao4 = async { usersDao.getAllEmails().fold("") { a, v -> "$a,$v" } }
        val dao5 = async { usersDao.getAll().joinToString(",") { it.email ?: "" } }
        val rep1 = async { userRepository.findByDisplayName("User1") }
        val rep2 = async { userRepository.findAllByEmailContains().firstOrNull() }
        val hello = """
            dao1:${dao1.await().id}
            dao2:${dao2.await()?.id}
            dao3:${dao3.await().id}
            dao4:${dao4.await()}
            dao5:${dao5.await()}
            rep1:${rep1.await()}
            rep2:${rep2.await()}
        """.trimIndent()
        logger.debug { "hello1 result $hello ${appConfig.baseUrl}" }
        return@coroutineScope hello
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
