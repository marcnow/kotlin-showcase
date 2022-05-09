package de.adesso.kotlinshowcase.cucumber

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(plugin = ["pretty"], features = ["src/test/resources/it/feature"])
class GreetingCucumber