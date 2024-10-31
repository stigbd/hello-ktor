package com.example.model

class FakeTaskRepository : TaskRepository {
    private val tasks =
        mutableListOf<Task>(
            Task("Task 1", "Description 1", Priority.LOW),
            Task("Task 2", "Description 2", Priority.MEDIUM),
            Task("Task 3", "Description 3", Priority.HIGH),
        )

    override fun allTasks(): List<Task> = tasks

    override fun taskByName(name: String): Task? = tasks.find { it.name.equals(name, ignoreCase = true) }
}
