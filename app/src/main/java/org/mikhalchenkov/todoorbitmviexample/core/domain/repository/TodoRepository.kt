package org.mikhalchenkov.todoorbitmviexample.core.domain.repository

import org.mikhalchenkov.todoorbitmviexample.core.domain.entity.Todo

interface TodoRepository {
    suspend fun getTodos(): List<Todo>
    suspend fun toggleTodo(id: Long)
    suspend fun addTodo(text: String): Todo
    suspend fun deleteTodo(id: Long)
}