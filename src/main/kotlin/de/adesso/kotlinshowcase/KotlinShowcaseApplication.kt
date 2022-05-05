package de.adesso.kotlinshowcase

import de.codecentric.boot.admin.server.config.EnableAdminServer
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.servers.Server
import io.swagger.v3.oas.models.servers.ServerVariable
import io.swagger.v3.oas.models.servers.ServerVariables
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
@EnableAdminServer
class KotlinShowcaseApplication

fun main(args: Array<String>) {
    runApplication<KotlinShowcaseApplication>(*args)
}

@Bean
fun customOpenAPI(): OpenAPI {
    return OpenAPI()
        .info(Info().title("Grreting API")
            .version("1.0.0")
            .description("Provides access to the API operations")
            .termsOfService("http://swagger.io/terms/")
            .license(License()
                .name("Apache 2.0")
                .url("http://www.apache.org/licenses/LICENSE-2.0.html")
            )
        )
        .addServersItem(Server()
                            .url("http://{host}:{port}")
                            .variables(ServerVariables()
                                .addServerVariable("host", ServerVariable()._default("localhost"))
                                .addServerVariable("port", ServerVariable()._default("8080"))
                            )
        )
}
