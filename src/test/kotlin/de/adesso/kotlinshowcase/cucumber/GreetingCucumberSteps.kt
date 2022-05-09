package de.adesso.kotlinshowcase.cucumber

import io.kotest.core.extensions.install
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.TestContainerExtension
import io.restassured.builder.RequestSpecBuilder
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.testcontainers.containers.Network
import org.testcontainers.containers.wait.strategy.Wait

class GreetingCucumberSteps : FunSpec ({

    lateinit var requestSpecification: RequestSpecification
    lateinit var response: Response

    val APPLICATION = install(TestContainerExtension("marcnow/kotlin-showcase:0.0.1-SNAPSHOT")) {
        withExposedPorts(8080)
        withNetwork(Network.newNetwork())
        withNetworkAliases("application")
        waitingFor(Wait.forHealthcheck()) //causes ContainerLaunchExceptions very irregular, why??
    }

    beforeTest {
        requestSpecification = RequestSpecBuilder()
            .setPort(APPLICATION.firstMappedPort)
            .build()
    }
})