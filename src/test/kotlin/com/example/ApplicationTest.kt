package com.example

import io.ktor.client.request.get
import io.ktor.server.config.MapApplicationConfig
import org.testcontainers.containers.PostgreSQLContainer

abstract class ApplicationTest {
    companion object {
        val postgreSQLContainer =
            PostgreSQLContainer<Nothing>("postgres:17").apply {
                withUsername("postgres")
                withDatabaseName("ktor_tutorial_db")
                withPassword("password")
                start()
            }
        val postgreSQLconfig =
            MapApplicationConfig(
                "storage.jdbcURL" to postgreSQLContainer.jdbcUrl,
                "storage.password" to postgreSQLContainer.password,
                "storage.user" to postgreSQLContainer.username,
            )
    }
}
