package name.stepin.config

import jakarta.ws.rs.container.ContainerRequestContext
import org.apache.logging.log4j.kotlin.Logging
import org.jboss.resteasy.reactive.server.ServerRequestFilter

class HttpRequestsLogger {

    companion object : Logging

    /**
     * Logs all incoming http requests.
     */
    @ServerRequestFilter(priority = 0)
    fun getFilter(ctx: ContainerRequestContext) {
        logger.debug {
            "Request: ${ctx.method} ${ctx.uriInfo.requestUri} headers: ${ctx.headers}"
        }
    }
}
