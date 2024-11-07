package com.example.model

import java.util.UUID

interface TaskRepository {
    suspend fun allTasks(): List<Task>

    suspend fun taskById(id: UUID): Task?

    suspend fun tasksByName(name: String): List<Task>

    suspend fun addTask(task: Task): Task
}
