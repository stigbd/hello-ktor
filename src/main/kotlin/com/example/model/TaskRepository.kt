package com.example.model

interface TaskRepository {
    fun allTasks(): List<Task>

    fun taskById(id: String): Task?

    fun taskByName(name: String): Task?
}
