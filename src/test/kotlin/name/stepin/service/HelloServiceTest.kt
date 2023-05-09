package name.stepin.service

import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import name.stepin.client.quarkus.extensions.QuarkusExtensionsClient
import name.stepin.config.AppConfig
import name.stepin.db.dao.UsersDao
import name.stepin.db.repository.UserRepository
import name.stepin.fixture.UserEntityFactory.userEntity
import name.stepin.fixture.UsersRecordFactory.usersRecord
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class HelloServiceTest {

    private lateinit var service: HelloService

    @MockK
    lateinit var usersDao: UsersDao

    @MockK
    lateinit var quarkusExtensionsClient: QuarkusExtensionsClient

    @MockK
    lateinit var appConfig: AppConfig

    @MockK
    lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp() {
        service = HelloService(usersDao, quarkusExtensionsClient, appConfig, userRepository)
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(usersDao, quarkusExtensionsClient, appConfig, userRepository)
    }

    @Test
    fun `hello empty case`() = runBlocking {
        coEvery { usersDao.getAll() } returns listOf()
        coEvery { usersDao.getAllEmails() } returns flowOf()
        coEvery { usersDao.byId(2) } returns usersRecord(2)
        coEvery { usersDao.byIdNullable(2) } returns null
        coEvery { usersDao.newRecord() } returns usersRecord(1000)
        coEvery { userRepository.findByDisplayName("User1") } returns userEntity(1)
        coEvery { userRepository.findAllByEmailContains("@example.com") } returns flowOf()
        every { appConfig.baseUrl } returns "greetingMessage1"
        val expected = """
            dao1:2
            dao2:null
            dao3:1000
            dao4:
            dao5:
            rep1:UserEntity{me1@example.com}
            rep2:null
        """.trimIndent()

        val actual = service.hello()

        assertEquals(expected, actual)
        coVerify(exactly = 1) { usersDao.getAll() }
        coVerify(exactly = 1) { usersDao.byId(2) }
        coVerify(exactly = 1) { usersDao.byIdNullable(2) }
        coVerify(exactly = 1) { usersDao.newRecord() }
        coVerify(exactly = 1) { usersDao.getAllEmails() }
        coVerify(exactly = 1) { userRepository.findByDisplayName("User1") }
        coVerify(exactly = 1) { userRepository.findAllByEmailContains("@example.com") }
        verify(exactly = 1) { appConfig.baseUrl }
    }

    @Test
    fun `hello main case`() = runBlocking {
        coEvery { usersDao.getAll() } returns listOf(
            usersRecord(1),
            usersRecord(2).apply { email = null },
        )
        coEvery { usersDao.getAllEmails() } returns flowOf("email1", "email2")
        coEvery { usersDao.byId(2) } returns usersRecord(2)
        coEvery { usersDao.byIdNullable(2) } returns usersRecord(2)
        coEvery { usersDao.newRecord() } returns usersRecord(1000)
        coEvery { userRepository.findByDisplayName("User1") } returns userEntity(1)
        coEvery { userRepository.findAllByEmailContains("@example.com") } returns flowOf(userEntity(1))
        every { appConfig.baseUrl } returns "greetingMessage1"
        val expected = """
            dao1:2
            dao2:2
            dao3:1000
            dao4:,email1,email2
            dao5:me1@example.com,
            rep1:UserEntity{me1@example.com}
            rep2:UserEntity{me1@example.com}
        """.trimIndent()

        val actual = service.hello()

        assertEquals(expected, actual)
        coVerify(exactly = 1) { usersDao.getAll() }
        coVerify(exactly = 1) { usersDao.byId(2) }
        coVerify(exactly = 1) { usersDao.byIdNullable(2) }
        coVerify(exactly = 1) { usersDao.newRecord() }
        coVerify(exactly = 1) { usersDao.getAllEmails() }
        coVerify(exactly = 1) { userRepository.findByDisplayName("User1") }
        coVerify(exactly = 1) { userRepository.findAllByEmailContains("@example.com") }
        verify(exactly = 1) { appConfig.baseUrl }
    }

    @Test
    fun `helloRemote main case`() = runBlocking {
        coEvery {
            quarkusExtensionsClient.getExtensionsById("io.quarkus:quarkus-resteasy-reactive")
        } returns setOf(
            QuarkusExtensionsClient.Extension(
                id = "io.quarkus:quarkus-resteasy-reactive",
                name = "Allyson Chandler",
                shortName = "Jesus Michael",
                keywords = listOf("kw1", "kw2"),
            ),
            QuarkusExtensionsClient.Extension(
                id = "io.quarkus:quarkus-resteasy-reactive2",
                name = "Allyson Chandler2",
                shortName = "Jesus Michael2",
                keywords = listOf("kw3", "kw4"),
            ),
        )

        val actual = service.helloRemote()

        assertEquals("Allyson Chandler, Allyson Chandler2", actual)
        coVerify(exactly = 1) {
            quarkusExtensionsClient.getExtensionsById("io.quarkus:quarkus-resteasy-reactive")
        }
    }
}
