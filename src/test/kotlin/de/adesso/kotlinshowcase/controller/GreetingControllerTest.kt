package de.adesso.kotlinshowcase.controller


import io.kotest.core.extensions.install
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.TestContainerExtension
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.specification.RequestSpecification
import org.hamcrest.Matchers
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.testcontainers.containers.Network
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait


class GreetingControllerTest : FunSpec ({

    val LOG = LoggerFactory.getLogger(GreetingController::class.java)
    lateinit var requestSpecification: RequestSpecification

    val APPLICATION = install(TestContainerExtension("marcnow/kotlin-showcase:0.0.1-SNAPSHOT")) {
        withExposedPorts(8080)
        withNetwork(Network.newNetwork())
        withNetworkAliases("application")
        waitingFor(Wait.forHealthcheck()) //causes ContainerLaunchExceptions very irregular, why??
    }

    beforeTest {
        APPLICATION.withLogConsumer(Slf4jLogConsumer(LOG))
        requestSpecification = RequestSpecBuilder()
            .setPort(APPLICATION.firstMappedPort)
            .build()
    }

    test("greet") {
        RestAssured.given(requestSpecification)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .pathParam("name", "Marc")
            .`when`()
            .get("/api/greet/{name}")
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body("message", Matchers.equalTo("Hello Marc!"))
    }

    test("greet the world") {
        RestAssured.given(requestSpecification)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .`when`()
            .get("/api/greet")
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body("message", Matchers.equalTo("Hello World!"))
    }

    test("get greeting") {
        RestAssured.given(requestSpecification)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .`when`()
            .get("/api/greet/greeting")
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body("greeting", Matchers.equalTo("Hello"))
    }

    test("update greeting") {
        RestAssured.given(requestSpecification)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body("{\"greeting\" : \"Hello\"}")
            .`when`()
            .put("/api/greet/greeting")
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value())
    }
})

