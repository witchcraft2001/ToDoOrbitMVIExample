package org.mikhalchenkov.todoorbitmviexample.features.todo_list.state

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.mikhalchenkov.todoorbitmviexample.core.domain.entity.Todo
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
            postSideEffect(TodoSideEffect.ShowError("Todo text cannot be empty"))
            return@intent
        }

        reduce { state.copy(isLoading = true, error = null) }

        try {
            val newTodo = todoRepository.addTodo(text.trim())

            reduce {
                state.copy(
                    todos = state.todos + newTodo,
                    isLoading = false,
                    newTodoText = ""
                )
            }

            postSideEffect(TodoSideEffect.ShowAddTodoSuccess)
            postSideEffect(TodoSideEffect.HideKeyboard)
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
                    }
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
                state.copy(isLoading = true)
            }
        } catch (e: Exception) {
            handleError(e)
        }
    }

    fun updateNewTodoText(text: String) = intent {
        reduce { state.copy(newTodoText = text) }
    }

    fun setFilter(filter: TodoFilter) = intent {
        reduce { state.copy(filter = filter) }
    }

    fun clearCompleted() = intent {
        val completedTodos = state.todos.filter { it.isCompleted }

        reduce {
            state.copy(todos = state.todos.filterNot { it.isCompleted })
        }

        try {
            completedTodos.forEach { todo ->
                todoRepository.deleteTodo(todo.id)
            }
        } catch (e: Exception) {
            // Revert state on error
            reduce {
                state.copy(todos = state.todos + completedTodos)
            }
            handleError(e)
        }
    }

    fun retryLoading() = intent {
        loadTodos()
    }

    private fun loadTodos() = intent {
        reduce { state.copy(isLoading = true, error = null) }

        try {
            val todos = todoRepository.getTodos()
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
        postSideEffect(TodoSideEffect.ShowError(exception.message ?: "Unknown error"))
    }

    val filteredTodos: List<Todo>
        get() = when (container.stateFlow.value.filter) {
            TodoFilter.ALL -> container.stateFlow.value.todos
            TodoFilter.ACTIVE -> container.stateFlow.value.todos.filter { !it.isCompleted }
            TodoFilter.COMPLETED -> container.stateFlow.value.todos.filter { it.isCompleted }
        }
}