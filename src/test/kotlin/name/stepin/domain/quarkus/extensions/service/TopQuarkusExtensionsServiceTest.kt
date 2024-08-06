package name.stepin.domain.quarkus.extensions.service

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.runBlocking
import name.stepin.domain.quarkus.extensions.client.QuarkusExtensionsNamesClient
import name.stepin.domain.quarkus.extensions.model.ExtensionName
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class TopQuarkusExtensionsServiceTest {
    private lateinit var service: TopQuarkusExtensionsService

    @MockK
    lateinit var quarkusExtensionsNamesClient: QuarkusExtensionsNamesClient

    @BeforeEach
    fun setUp() {
        service = TopQuarkusExtensionsService(quarkusExtensionsNamesClient)
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(quarkusExtensionsNamesClient)
    }

    @Test
    fun `getTopExtension empty case`() =
        runBlocking {
            coEvery { quarkusExtensionsNamesClient.getNames() } returns listOf()

            val actual = service.getTopExtension()

            assertEquals(null, actual)
            coVerify(exactly = 1) { quarkusExtensionsNamesClient.getNames() }
        }

    @Test
    fun `getTopExtension main case`() =
        runBlocking {
            val expected = ExtensionName("extensionName1")
            coEvery { quarkusExtensionsNamesClient.getNames() } returns
                listOf(
                    expected,
                    ExtensionName("extensionName2"),
                )

            val actual = service.getTopExtension()

            assertEquals(expected, actual)
            coVerify(exactly = 1) { quarkusExtensionsNamesClient.getNames() }
        }
}
