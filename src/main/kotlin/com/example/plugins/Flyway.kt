package com.example.plugins

import io.ktor.server.application.Application
import io.ktor.server.config.ApplicationConfig
import org.flywaydb.core.Flyway

fun Application.configureFlyway(config: ApplicationConfig) {
    val url = config.property("storage.jdbcURL").getString()
    val user = config.property("storage.user").getString()
    val password = config.property("storage.password").getString()

    Flyway.configure()
        .dataSource(
            url,
            user,
            password,
        ).load()
        .migrate()
}
