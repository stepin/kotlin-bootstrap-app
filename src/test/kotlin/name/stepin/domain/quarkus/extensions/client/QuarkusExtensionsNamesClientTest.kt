package name.stepin.domain.quarkus.extensions.client

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.runBlocking
import name.stepin.client.quarkus.extensions.QuarkusExtensionsClient
import name.stepin.domain.quarkus.extensions.model.ExtensionName
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class QuarkusExtensionsNamesClientTest {

    private lateinit var service: QuarkusExtensionsNamesClient

    @MockK
    lateinit var client: QuarkusExtensionsClient

    @BeforeEach
    fun setUp() {
        service = QuarkusExtensionsNamesClient(client)
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(client)
    }

    @Test
    fun `getNames main case`() = runBlocking {
        coEvery { client.getExtensionsById("io.quarkus:quarkus-resteasy-reactive") } returns setOf(
            QuarkusExtensionsClient.Extension(
                id = "io.quarkus:quarkus-resteasy-reactive",
                name = "name1",
                shortName = "Jesus Michael",
                keywords = listOf("kw1", "kw2"),
            ),
            QuarkusExtensionsClient.Extension(
                id = "io.quarkus:quarkus-resteasy-reactive2",
                name = "name2",
                shortName = "Jesus Michael2",
                keywords = listOf("kw3", "kw4"),
            ),
        )
        val expected = listOf(
            ExtensionName("name1"),
            ExtensionName("name2"),
        )

        val actual = service.getNames()

        assertEquals(expected, actual)
        coVerify(exactly = 1) { client.getExtensionsById(any()) }
    }
}
