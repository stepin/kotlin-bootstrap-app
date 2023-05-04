package name.stepin.client.quarkus.extensions

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import kotlinx.coroutines.runBlocking
import name.stepin.config.WebClientFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@WireMockTest
class QuarkusExtensionsClientTest {

    @Test
    fun `getExtensionsById main case`(
        wireMockRuntimeInfo: WireMockRuntimeInfo,
    ) = runBlocking {
        wireMockRuntimeInfo.wireMock.register(
            WireMock.get("/extensions?id=1234")
                .willReturn(
                    WireMock.okJson(
                        """
                            [
                                {
                                    "id" : "io.quarkus:quarkus-resteasy-reactive",
                                    "name" : "Allyson Chandler",
                                    "shortName" : "Jesus Michael",
                                    "keywords" : ["kw1", "kw2"]
                                },
                                {
                                    "id" : "io.quarkus:quarkus-resteasy-reactive2",
                                    "name" : "Allyson Chandler2",
                                    "shortName" : "Jesus Michael2",
                                    "keywords" : ["kw3", "kw4"]
                                }
                            ]
                        """.trimIndent(),
                    ),
                ),
        )
        val expected = setOf(
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

        val client = createClient(wireMockRuntimeInfo.httpPort)
        val actual = client.getExtensionsById("1234")

        assertEquals(expected, actual)
    }

    private fun createClient(port: Int) = WebClientFactory.createClient(
        baseUrl = "http://localhost:$port",
        serviceType = QuarkusExtensionsClient::class.java,
    )
}
