package de.adesso.kotlinshowcase.health

import de.adesso.kotlinshowcase.controller.GreetingController
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.core.env.Environment
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Component
class GreetControllerHealthIndicator(val environment: Environment) : HealthIndicator {

    private val SERVER_PORT = "local.server.port"

    override fun health(): Health {
        val builder = Health.Builder()

        val headers = HttpHeaders()
        headers.set("ORIGIN", String.format("%s:%s", "localhost", environment.getProperty(SERVER_PORT)))

        val entity = HttpEntity<String>(headers)

        val response = RestTemplate().exchange(getResourceUri(), HttpMethod.OPTIONS, entity, String::class.java)

        val up = HttpStatus.OK.equals(response.statusCode)

        if (up) {
            builder.withDetail("resource", "available").up()
        } else {
            builder.withDetail("resource", "not available").down()
        }

        return builder.build()
    }

    private fun getResourceUri(): URI =
        UriComponentsBuilder.newInstance()
            .scheme("http")
            .host("localhost")
            .port(environment.getProperty(SERVER_PORT))
            .path(GreetingController::class.java.getAnnotation(RequestMapping::class.java).value[0])
            .build()
            .toUri()

}