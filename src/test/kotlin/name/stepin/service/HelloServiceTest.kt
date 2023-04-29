package name.stepin.service

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.runBlocking
import name.stepin.UsersRecordFactory.usersRecord
import name.stepin.client.test.QuarkusExtensionsClient
import name.stepin.db.dao.UsersDao
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

    @BeforeEach
    fun setUp() {
        service = HelloService(
            usersDao = usersDao,
            quarkusExtensionsClient = quarkusExtensionsClient
        )
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(
            usersDao
        )
    }

    @Test
    fun `hello empty case`() = runBlocking {
        coEvery { usersDao.getAll() } returns listOf()
        val expected = ""

        val actual = service.hello()

        assertEquals(expected, actual)
        coVerify(exactly = 1) { usersDao.getAll() }
    }

    @Test
    fun `hello main case`() = runBlocking {
        coEvery { usersDao.getAll() } returns listOf(
            usersRecord(1),
            usersRecord(2)
        )
        val expected = "me1@example.com, me2@example.com"

        val actual = service.hello()

        assertEquals(expected, actual)
        coVerify(exactly = 1) { usersDao.getAll() }
    }
}
