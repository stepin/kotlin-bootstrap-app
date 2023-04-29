package name.stepin.web

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import name.stepin.service.HelloService
import name.stepin.web.model.HelloRemoteResponse

@Path("/api")
class GreetingController(
    private val helloService: HelloService
) {
    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    suspend fun hello() = "Hello RESTEasy ${helloService.hello()}"

    @GET
    @Path("/helloRemote")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun helloRemote(): HelloRemoteResponse {
        val message = "Hello RESTEasy ${helloService.helloRemote()}"

        return HelloRemoteResponse(message)
    }
}
