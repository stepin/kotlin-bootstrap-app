package name.stepin.client.test

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.QueryParam
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient

@RegisterRestClient(baseUri = "https://stage.code.quarkus.io/api")
interface QuarkusExtensionsClient {

    @GET
    @Path("/extensions")
    suspend fun getExtensionsById(@QueryParam("id") id: String): Set<Extension>

    data class Extension(
        val id: String,
        val name: String,
        val shortName: String,
        val keywords: List<String>
    )
}
