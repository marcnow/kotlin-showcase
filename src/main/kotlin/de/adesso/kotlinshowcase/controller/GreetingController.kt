package de.adesso.kotlinshowcase.controller

import de.adesso.kotlinshowcase.controller.dto.GreetDTO
import de.adesso.kotlinshowcase.controller.dto.GreetingDTO
import de.adesso.kotlinshowcase.service.GreetingService
import io.micrometer.core.annotation.Timed
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/greet")
@Timed(value = "Greeting", description = "Metrics of the GreetingController")
class GreetingController(val service: GreetingService) {

    private val LOG = LoggerFactory.getLogger(GreetingController::class.java)

    @GetMapping("/{name}")
    @Operation(operationId = "greetSomeone", description = "Greet someone")
    @ApiResponse(responseCode = "200", description = "Ok", content = [Content(schema = Schema(implementation = GreetDTO::class))])
    fun greet(@Parameter(description = "name") @PathVariable name: String): ResponseEntity<GreetDTO> {
        LOG.info("Greet {}", name)

        val message = GreetDTO(service.getMessage(name))

        LOG.info("{}", message)

        return ResponseEntity.status(HttpStatus.OK).body(message)
    }

    @GetMapping
    @Operation(operationId = "greetSomeone", description = "Greet the world")
    @ApiResponse(responseCode = "200", description = "Ok", content = [Content(schema = Schema(implementation = GreetDTO::class))])
    fun greetTheWorld(): ResponseEntity<GreetDTO> = greet("World")

    @GetMapping("/greeting")
    @Operation(operationId = "getGreeting", description = "Get greeting")
    @ApiResponse(responseCode = "200", description = "Ok", content = [Content(schema = Schema(implementation = GreetingDTO::class))])
    fun getGreeting(): ResponseEntity<GreetingDTO> {
        LOG.info("Get greeting")

        val greeting = GreetingDTO(service.getGreeting())

        LOG.info("{}", greeting)

        return ResponseEntity.status(HttpStatus.OK).body(greeting)
    }

    @PutMapping(consumes = ["application/json"])
    @Operation(operationId = "updateGreeting", description = "Update greeting")
    @RequestBody(description = "greeting", required = true, content = [Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = Schema(implementation = GreetingDTO::class, type = "OBJECT", example = "{\"greeting\"} : \"Hola\""))])
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Greeting updated"),
        ApiResponse(responseCode = "404", description = "Invalid 'greeting' request")])
    fun updateGreeting(@org.springframework.web.bind.annotation.RequestBody greeting: GreetingDTO): ResponseEntity<HttpStatus> {
        LOG.info("Set greeting to {}", greeting.greeting)

        service.updateMessage(greeting.greeting)

        LOG.info("Greeting updated")

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }


}