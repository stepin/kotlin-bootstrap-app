package name.stepin.graphql

import com.ninjasquad.springmockk.MockkBean
import graphql.ErrorType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.junit5.MockKExtension
import name.stepin.graphql.model.HelloRemoteResult
import name.stepin.graphql.model.ResultStatus
import name.stepin.service.HelloService
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.graphql.test.tester.GraphQlTester

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(MockKExtension::class)
@AutoConfigureGraphQlTester
class HelloGraphQLTest {
    @MockkBean
    private lateinit var helloService: HelloService

    @Autowired
    lateinit var graphQlTester: GraphQlTester

    @AfterEach
    fun tearDown() {
        confirmVerified(helloService)
    }

    @Test
    fun `sayHello failed name validation case`() {
        @Language("GraphQL")
        val document =
            """
            query sayHello(${'$'}name: String!){
                sayHello(name : ${'$'}name)
            }
            """.trimIndent()

        val response =
            graphQlTester.document(document)
                .variable("name", "n1")
                .execute()

        response.errors()
            .expect {
                it.path == "sayHello" &&
                    it.message?.contains("от 3") == true &&
                    it.message?.startsWith("sayHello.name:") == true &&
                    it.errorType == ErrorType.ValidationError
            }
            .verify()
    }

    @Test
    fun `sayHello main case`() {
        @Language("GraphQL")
        val document =
            """
            query sayHello(${'$'}name: String!){
                sayHello(name : ${'$'}name)
            }
            """.trimIndent()
        coEvery { helloService.hello() } returns "hello1"

        val actual =
            graphQlTester.document(document)
                .variable("name", "name1")
                .execute()
                .path("$.data.sayHello")
                .entity(String::class.java)
                .get()

        assertEquals("Hello GraphQL name1, hello1", actual)
        coVerify(exactly = 1) { helloService.hello() }
    }

    @Test
    fun `helloRemote main case`() {
        @Language("GraphQL")
        val document =
            """
            query {
                helloRemote { message status }
            }
            """.trimIndent()
        val expected =
            HelloRemoteResult(
                status = ResultStatus.SUCCESS,
                message = "helloRemote1",
            )
        coEvery { helloService.helloRemote() } returns "helloRemote1"

        val response =
            graphQlTester.document(document)
                .execute()
                .path("$.data.helloRemote")
                .entity(HelloRemoteResult::class.java)

        response.isEqualTo(expected)
        coVerify(exactly = 1) { helloService.helloRemote() }
    }

    @Test
    fun `hello2 main case`() {
        @Language("GraphQL")
        val document =
            """
            query {
                hello2
            }
            """.trimIndent()

        val response =
            graphQlTester.document(document)
                .execute()
                .path("$.data.hello2")
                .entity(String::class.java)

        response.isEqualTo("hello2")
    }
}
