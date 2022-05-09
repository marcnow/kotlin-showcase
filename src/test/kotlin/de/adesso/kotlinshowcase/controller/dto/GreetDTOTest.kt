package de.adesso.kotlinshowcase.controller.dto

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class GreetDTOTest : FunSpec({

    test("instantiation should succeed") {
        val greet = GreetDTO("Hello Marc!")
        greet.message shouldBe "Hello Marc!"
    }
})