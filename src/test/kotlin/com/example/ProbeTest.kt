package com.example

import com.example.model.PostgresTaskRepository
import com.example.plugins.configureDatabases
import com.example.plugins.configureFlyway
import com.example.plugins.configureRouting
import com.example.plugins.configureSerialization
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class ProbeTest : ApplicationTest() {
    @Test
    fun `get of liveness returns http status OK`() =
        testApplication {
            environment {
                config = postgreSQLconfig
            }
            application {
                val repository = PostgresTaskRepository()

                configureDatabases(environment.config)
                configureSerialization(repository)
                configureFlyway(environment.config)
                configureRouting()
            }
            val response = client.get("/liveness")
            assertEquals(HttpStatusCode.OK, response.status)
        }

    @Test
    fun `get of readiness return http status OK`() =
        testApplication {
            environment {
                config = postgreSQLconfig
            }
            application {
                val repository = PostgresTaskRepository()

                configureDatabases(environment.config)
                configureSerialization(repository)
                configureFlyway(environment.config)
                configureRouting()
            }
            val response = client.get("/readiness")
            assertEquals(HttpStatusCode.OK, response.status)
        }
}
