package de.adesso.kotlinshowcase.controller

import de.adesso.kotlinshowcase.controller.dto.GreetDTO
import de.adesso.kotlinshowcase.controller.dto.GreetingDTO
import de.adesso.kotlinshowcase.service.GreetingService
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.springframework.http.HttpStatus

class GreetingControllerUnitTest : FunSpec ({

    val service: GreetingService = mockk()
    val controller = GreetingController(service)

    beforeEach {
        clearAllMocks()
    }

    test("greet") {
        //given
        every { service.getMessage("Marc") } returns "Hola Marc!"

        //when
        val response = controller.greet("Marc")
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe GreetDTO("Hola Marc!")

        //then
        verifyAll { service.getMessage(any()) }
    }

    test("greet the world") {
        every { service.getMessage("World") } returns "Hello World!"

        val response = controller.greetTheWorld()
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe GreetDTO("Hello World!")

        verifyAll { service.getMessage(any()) }
    }

    test("get greeting") {
        every { service.getGreeting() } returns "Hola"

        val response = controller.getGreeting()
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe GreetingDTO("Hola")

        verifyAll { service.getGreeting() }
    }

    test("update greeting") {
        every { service.updateMessage(any()) } just Runs

        val response = controller.updateGreeting(GreetingDTO("Hola"))
        response.statusCode shouldBe HttpStatus.NO_CONTENT

        verifyAll { service.updateMessage(any()) }
    }


})