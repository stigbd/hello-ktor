package com.example.model

interface TaskRepository {
    fun allTasks(): List<Task>

    fun taskByName(name: String): Task?
}
