package name.stepin.web

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test

@QuarkusTest
class GreetingControllerTest {

    @Test
    fun testHelloEndpoint() {
        given()
            .`when`().get("/api/hello")
            .then()
            .statusCode(200)
            .body(`is`("Hello RESTEasy "))
    }
}
