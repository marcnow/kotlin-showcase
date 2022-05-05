package de.adesso.kotlinshowcase.service.impl

import de.adesso.kotlinshowcase.model.ApplicationProperties
import de.adesso.kotlinshowcase.service.GreetingService
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicReference

@Service
class GreetingServiceImpl(properties: ApplicationProperties) : GreetingService {

    private val greeting = AtomicReference<String>()

    init {
        this.greeting.set(properties.getGreeting())
    }

    override fun getGreeting(): String = greeting.get()

    override fun getMessage(who: String): String = String.format("%s %s!", getGreeting(), who)

    override fun updateMessage(greeting: String) = this.greeting.set(greeting)

}