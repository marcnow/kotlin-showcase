package de.adesso.kotlinshowcase.service.impl

import de.adesso.kotlinshowcase.model.ApplicationProperties
import de.adesso.kotlinshowcase.service.GreetingService
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class GreetingServiceTest : FunSpec ({

    lateinit var service: GreetingService

    beforeEach {
        val properties = ApplicationProperties()
        properties.setGreeting("Hello")
        service = GreetingServiceImpl(properties)
    }

    test("get greeting") {
        service.getGreeting() shouldBe "Hello"
    }

    test("update greeting") {
        service.updateMessage("Hola")
        service.getGreeting() shouldBe "Hola"
    }

    test("get Message") {
        service.getMessage("World") shouldBe "Hello World!"
    }
})