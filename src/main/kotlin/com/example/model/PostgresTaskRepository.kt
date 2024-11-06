package com.example.model

import com.example.db.TaskDAO
import com.example.db.TaskTable
import com.example.db.daoToModel
import com.example.db.suspendTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.UUID

class PostgresTaskRepository : TaskRepository {
    override suspend fun addTask(task: Task): Task =
        suspendTransaction {
            TaskDAO.new {
                name = task.name
                description = task.description
                priority = task.priority.toString()
            }.let(::daoToModel)
        }

    override suspend fun allTasks(): List<Task> =
        suspendTransaction {
            TaskDAO.all().map(::daoToModel)
        }

    override suspend fun taskById(id: UUID): Task? =
        suspendTransaction {
            TaskDAO
                .findById(id)
                ?.let(::daoToModel)
        }

    override suspend fun tasksByName(name: String): List<Task> =
        suspendTransaction {
            TaskDAO
                .find { (TaskTable.name eq name) }
                .map(::daoToModel)
                .orEmpty()
        }
}
