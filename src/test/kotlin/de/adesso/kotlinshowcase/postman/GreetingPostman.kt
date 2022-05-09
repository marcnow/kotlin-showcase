package de.adesso.kotlinshowcase.postman

import io.kotest.core.extensions.install
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.TestContainerExtension
import io.kotest.matchers.shouldBe
import org.slf4j.LoggerFactory
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.Network
import org.testcontainers.containers.startupcheck.OneShotStartupCheckStrategy
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.MountableFile
import java.time.Duration

class GreetingPostman : FunSpec ({

    val LOG = LoggerFactory.getLogger(GreetingPostman::class.java)

    val APPLICATION = install(TestContainerExtension("marcnow/kotlin-showcase:0.0.1-SNAPSHOT")) {
        withExposedPorts(8080)
        withNetwork(Network.newNetwork())
        withNetworkAliases("application")
        waitingFor(Wait.forHealthcheck()) //causes ContainerLaunchExceptions very irregular, why??
    }

    val NEWMAN = install(TestContainerExtension("postman/newman:5.1.0-alpine")) {
        withNetwork(Network.newNetwork())
        dependsOn(APPLICATION)
        withCopyFileToContainer(MountableFile.forClasspathResource("postman/kotlin-showcase.postman_collection.json")
            ,("etc/newman/kotlin-showcase.postman_collection.json"))
        withCopyFileToContainer(MountableFile.forClasspathResource("postman/kotlin-showcase.postman_environment.json")
            ,("etc/newman/kotlin-showcase.postman_environment.json"))
        withFileSystemBind("target/postman/reports", "/etc/newman/reports", BindMode.READ_WRITE)
        withStartupCheckStrategy(OneShotStartupCheckStrategy().withTimeout(Duration.ofSeconds(5)))
    }

    test("run") {
        NEWMAN.withCommand("run", "kotlin-showcase.postman_collection.json",
            "--environment=kotlin-showcase.postman_environment.json",
            "--reporters=cli,junit",
            "--reporter-junit-export=reports/kotlin-showcase.newman-report.xml")
        NEWMAN.start()

        LOG.info(NEWMAN.getLogs())

        NEWMAN.currentContainerInfo.state.exitCode shouldBe 0
    }
})