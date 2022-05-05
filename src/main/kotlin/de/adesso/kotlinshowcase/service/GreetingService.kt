package de.adesso.kotlinshowcase.service

interface GreetingService {

    fun getGreeting(): String

    fun getMessage(who: String): String

    fun updateMessage(greeting: String)
}