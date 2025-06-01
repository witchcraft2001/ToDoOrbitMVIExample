package org.mikhalchenkov.todoorbitmviexample.features.todo_list.state

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import org.mikhalchenkov.todoorbitmviexample.core.domain.repository.TodoRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    savedStateHandle: SavedStateHandle
) : ContainerHost<TodoState, TodoSideEffect>, ViewModel() {

    override val container = container<TodoState, TodoSideEffect>(
        initialState = TodoState(),
        savedStateHandle = savedStateHandle
    )

    init {
        loadTodos()
    }

    fun addTodo(text: String) = intent {
        if (text.isBlank()) {
            postSideEffect(TodoSideEffect.ShowMessage("Todo text cannot be empty"))
            return@intent
        }

        reduce { state.copy(isLoading = true, error = null) }

        try {
            val newTodo = todoRepository.addTodo(text.trim())

            reduce {
                state.copy(
                    todos = (state.todos + newTodo).toImmutableList(),
                    isLoading = false,
                    newTodoText = ""
                )
            }

            postSideEffect(TodoSideEffect.ShowMessage("Todo added successfully"))
        } catch (e: Exception) {
            handleError(e)
        }
    }

    fun toggleTodo(todoId: Long) = intent {
        val todoToUpdate = state.todos.find { it.id == todoId } ?: return@intent

        reduce {
            state.copy(
                isLoading = true,
                error = null,
            )
        }

        try {
            todoRepository.toggleTodo(todoId)
            val updatedTodo = todoToUpdate.copy(isCompleted = !todoToUpdate.isCompleted)
            reduce {
                state.copy(
                    isLoading = false,
                    error = null,
                    todos = state.todos.map { todo ->
                        if (todo.id == todoId) updatedTodo else todo
                    }.toImmutableList()
                )
            }
        } catch (e: Exception) {
            handleError(e)
        }
    }

    fun deleteTodo(todoId: Long) = intent {
        reduce {
            state.copy(isLoading = true)
        }

        try {
            todoRepository.deleteTodo(todoId)
            reduce {
                state.copy(
                    isLoading = false,
                    todos = state.todos.filterNot { it.id == todoId }.toImmutableList(),
                )
            }
        } catch (e: Exception) {
            handleError(e)
        }
    }

    private fun loadTodos() = intent {
        reduce { state.copy(isLoading = true, error = null) }

        try {
            val todos = todoRepository.getTodos().toImmutableList()
            reduce {
                state.copy(todos = todos, isLoading = false)
            }
        } catch (e: Exception) {
            handleError(e)
        }
    }

    private fun handleError(exception: Exception) = intent {
        reduce {
            state.copy(isLoading = false, error = exception.message)
        }
        postSideEffect(TodoSideEffect.ShowMessage(exception.message ?: "Unknown error"))
    }
}