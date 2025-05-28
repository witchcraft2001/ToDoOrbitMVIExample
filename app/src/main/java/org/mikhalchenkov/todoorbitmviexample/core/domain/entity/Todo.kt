package org.mikhalchenkov.todoorbitmviexample.core.domain.entity

data class Todo(
    val id: Long,
    val text: String,
    val isCompleted: Boolean = false,
)
