package de.adesso.kotlinshowcase.controller.dto

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class GreetingDTOTest : FunSpec ({
    test("instantiation should succeed") {
        val greeting = GreetingDTO("Hola")
        greeting.greeting shouldBe "Hola"
    }

    test("set greeting") {
        val greeting = GreetingDTO("")
        greeting.greeting = "Hola"
        greeting.greeting shouldBe "Hola"
    }
})