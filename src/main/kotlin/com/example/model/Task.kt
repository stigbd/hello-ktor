package com.example.model

import kotlinx.serialization.Serializable

enum class Priority {
    LOW,
    MEDIUM,
    HIGH,
}

@Serializable
data class Task(val name: String, val description: String, val priority: Priority)
