package de.adesso.kotlinshowcase.health

import io.kotest.core.extensions.install
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.TestContainerExtension
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import org.hamcrest.Matchers
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.testcontainers.containers.Network
import org.testcontainers.containers.wait.strategy.Wait

class GreetControllerHealthIndicatorTest : FunSpec ({

    val APPLICATION = install(TestContainerExtension("marcnow/kotlin-showcase:0.0.1-SNAPSHOT")) {
        withExposedPorts(8080)
        withNetwork(Network.newNetwork())
        withNetworkAliases("application")
        waitingFor(Wait.forHealthcheck()) //causes ContainerLaunchExceptions very irregular, why??
    }

    val requestSpecification = RequestSpecBuilder()
        .setPort(APPLICATION.firstMappedPort)
        .build()

    test("check health") {
        RestAssured.given(requestSpecification)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .`when`()
            .get("/actuator/health")
            .then()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .statusCode(HttpStatus.OK.value())
            .body("status", Matchers.equalTo("UP"))
            .rootPath("components.greetController")
            .body("status", Matchers.equalTo("UP"))
    }
})