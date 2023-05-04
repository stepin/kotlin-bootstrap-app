package name.stepin.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.junit5.MockKExtension
import name.stepin.exception.EntityNotFoundException
import name.stepin.service.HelloService
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriComponentsBuilder

@ExtendWith(MockKExtension::class)
@WebFluxTest(controllers = [GreetingController::class])
class GreetingControllerTest {

    @MockkBean
    private lateinit var helloService: HelloService

    @Autowired
    lateinit var client: WebTestClient

    @AfterEach
    fun tearDown() {
        confirmVerified(helloService)
    }

    @Test
    fun `hello 404 case`() {
        coEvery { helloService.hello() } throws EntityNotFoundException()

        val response = client.get()
            .uri("/api/hello?name=myName1")
            .accept(MediaType.TEXT_PLAIN)
            .exchange()

        response
            .expectStatus().isNotFound
        coVerify(exactly = 1) { helloService.hello() }
    }

    @Test
    fun `hello constraints violation case`() {
        val response = client.get()
            .uri("/api/hello?name=n1")
            .accept(MediaType.TEXT_PLAIN)
            .exchange()

        response
            .expectStatus().isBadRequest
    }

    @Test
    fun `hello main case`() {
        coEvery { helloService.hello() } returns "hello1"

        val response = client.get()
            .uri("/api/hello?name=myName1")
            .accept(MediaType.TEXT_PLAIN)
            .exchange()

        response
            .expectStatus().isOk
            .expectBody(String::class.java)
            .consumeWith {
                assertEquals("Hello RESTEasy myName1, hello1", it.responseBody)
            }
        coVerify(exactly = 1) { helloService.hello() }
    }

    @Test
    fun `helloRemote main case`() {
        coEvery { helloService.helloRemote() } returns "hello1"

        val response = client.get()
            .uri("/api/helloRemote")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()

        response
            .expectStatus().isOk
            .expectBody()
            .json(
                """
                {
                "message":"Hello RESTEasy hello1"
                }
                """.trimIndent(),
            )
        coVerify(exactly = 1) { helloService.helloRemote() }
    }

    @Test
    fun `testArguments main case`() {
        @Language("JSON")
        val body = """
            {
            "some-thing": "some-value-1"
            }
        """.trimIndent()
        val response = client.post()
            .uri(
                UriComponentsBuilder.fromUriString("/api/hello/1234")
                    .queryParam("myQueryParam", "value1")
                    .build().toUri(),
            )
            .header("my-header", "value1")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()

        response
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.message").isEqualTo("message: 1234 value1 value1 TestArgumentsRequest(someThing=some-value-1)")
    }
}
