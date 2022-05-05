package de.adesso.kotlinshowcase.model

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "app")
class ApplicationProperties {

    private var greeting = String()

    fun getGreeting(): String = greeting

    fun setGreeting(greeting: String) {
        this.greeting = greeting
    }
}