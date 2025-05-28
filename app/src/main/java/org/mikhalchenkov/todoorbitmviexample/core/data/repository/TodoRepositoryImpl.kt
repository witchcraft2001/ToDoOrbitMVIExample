package org.mikhalchenkov.todoorbitmviexample.core.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.mikhalchenkov.todoorbitmviexample.core.domain.entity.Todo
import org.mikhalchenkov.todoorbitmviexample.core.domain.repository.TodoRepository
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor() : TodoRepository {
    private var todoIdCounter = 0L
    private val todos = mutableListOf<Todo>()

    override suspend fun getTodos(): List<Todo> {
        return withContext(Dispatchers.IO) {
            delay(500) // Simulate a network request
            todos.toList()
        }
    }

    override suspend fun toggleTodo(id: Long) {
        return withContext(Dispatchers.IO) {
            delay(200) // Simulate a network request
            val index = todos.indexOfFirst { it.id == id }
            if (index != -1) {
                todos[index] = todos[index].copy(isCompleted = !todos[index].isCompleted)
            }
        }
    }

    override suspend fun addTodo(text: String): Todo {
        return withContext(Dispatchers.IO) {
            delay(200) // Simulate a network request
            val todo = Todo(
                id = ++todoIdCounter,
                text = text,
                isCompleted = false
            )
            todos.add(todo)
            todo
        }
    }

    override suspend fun deleteTodo(id: Long) {
        return withContext(Dispatchers.IO) {
            delay(200) // Simulate a network request
            todos.removeAll { it.id == id }
        }
    }
}