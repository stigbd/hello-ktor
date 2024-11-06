package com.example

import com.example.db.TaskTable
import com.example.model.PostgresTaskRepository
import com.example.model.Priority
import com.example.model.Task
import com.example.plugins.configureDatabases
import com.example.plugins.configureFlyway
import com.example.plugins.configureRouting
import com.example.plugins.configureSerialization
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TasksTest : ApplicationTest() {
    // Insert some data before each test:

    @BeforeTest
    fun setUp() {
        // Create table and insert test data
        transaction {
            SchemaUtils.create(TaskTable)
            TaskTable.insert {
                it[id] = UUID.fromString("00000000-0000-0000-0000-000000000001")
                it[name] = "Task 1"
                it[description] = "Description 1"
                it[priority] = "Low"
            }
            TaskTable.insert {
                it[id] = UUID.fromString("00000000-0000-0000-0000-000000000002")
                it[name] = "Task 2"
                it[description] = "Description 2"
                it[priority] = "High"
            }
        }
    }

    @AfterTest
    fun tearDown() {
        // Drop table
        transaction {
            SchemaUtils.drop(TaskTable)
        }
    }

    @Test
    fun `add task should return status Created`() =
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
            val client =
                createClient {
                    install(ContentNegotiation) {
                        json()
                    }
                }
            val task =
                Task(
                    name = "Test Task",
                    description = "This is a test task",
                    priority = Priority.High,
                )
            val response =
                client.post("tasks") {
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                    setBody(task)
                }
            assertEquals(HttpStatusCode.Created, response.status)
        }

    @Test
    fun `get all tasks should return OK and a list of two tasks`() =
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
            val client =
                createClient {
                    install(ContentNegotiation) {
                        json()
                    }
                }
            val response = client.get("tasks")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals("application/json; charset=UTF-8", response.headers["Content-Type"])
            val tasks = response.body<List<Task>>()
            assertEquals(2, tasks.size)
        }

    @Test
    fun `get a task by id should return the task`() =
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
            val client =
                createClient {
                    install(ContentNegotiation) {
                        json()
                    }
                }
            val response = client.get("tasks/00000000-0000-0000-0000-000000000001")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals("application/json; charset=UTF-8", response.headers["Content-Type"])
        }

    @Test
    fun `get all tasks with query paramter name should return a list of one task`() =
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
            val client =
                createClient {
                    install(ContentNegotiation) {
                        json()
                    }
                }
            val response = client.get("tasks?name=Task%201")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals("application/json; charset=UTF-8", response.headers["Content-Type"])
            assertEquals(1, response.body<List<Task>>().size)
            assertEquals("Task 1", response.body<List<Task>>()[0].name)
        }

    @Test
    fun `get all tasks with unknown query parameter name should return Ok and empty list`() =
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
            val client =
                createClient {
                    install(ContentNegotiation) {
                        json()
                    }
                }
            val response = client.get("tasks?name=unknown")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals("application/json; charset=UTF-8", response.headers["Content-Type"])
            assertTrue(response.body<List<Task>>() == emptyList<Task>())
        }

    @Test
    fun `get a task by random id should return NotFound`() =
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
            val uuid = UUID.randomUUID()
            val client =
                createClient {
                    install(ContentNegotiation) {
                        json()
                    }
                }
            val response = client.get("tasks/$uuid")
            assertEquals(HttpStatusCode.NotFound, response.status)
        }

    @Test
    fun `post a task with empty name should return Bad Request`() =
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
            val client =
                createClient {
                    install(ContentNegotiation) {
                        json()
                    }
                }
            val task =
                Task(
                    name = "Test Task",
                    description = "This is a test task",
                    priority = Priority.High,
                )
            val response =
                client.post("tasks") {
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                    setBody(task.copy(name = ""))
                }
            assertEquals(HttpStatusCode.BadRequest, response.status)
        }

    @Test
    fun `post another task with same name should return Bad Request `() =
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
            val client =
                createClient {
                    install(ContentNegotiation) {
                        json()
                    }
                }
            val task =
                Task(
                    name = "Duplicate Task",
                    description = "This is a test task",
                    priority = Priority.High,
                )
            val response =
                client.post("tasks") {
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                    setBody(task)
                }
            assertEquals(HttpStatusCode.Created, response.status)
            val response2 =
                client.post("tasks") {
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                    setBody(task)
                }
            assertEquals(HttpStatusCode.BadRequest, response2.status)
        }

    @Test
    fun `post a task with bad json should return Bad Request`() =
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
            val client =
                createClient {
                    install(ContentNegotiation) {
                        json()
                    }
                }
            val response =
                client.post("tasks") {
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                    setBody("bad json")
                }
            assertEquals(HttpStatusCode.BadRequest, response.status)
        }
}
