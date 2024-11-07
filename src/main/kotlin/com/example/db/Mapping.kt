package com.example.db

import com.example.model.Priority
import com.example.model.Task
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID

object TaskTable : UUIDTable("task") {
    val name = varchar("name", 50)
    val description = varchar("description", 50)
    val priority = varchar("priority", 50)
}

class TaskDAO(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<TaskDAO>(TaskTable)

    var name by TaskTable.name
    var description by TaskTable.description
    var priority by TaskTable.priority
}

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T = newSuspendedTransaction(Dispatchers.IO, statement = block)

fun daoToModel(dao: TaskDAO) =
    Task(
        dao.name,
        dao.description,
        Priority.valueOf(dao.priority),
        dao.id.value,
    )
