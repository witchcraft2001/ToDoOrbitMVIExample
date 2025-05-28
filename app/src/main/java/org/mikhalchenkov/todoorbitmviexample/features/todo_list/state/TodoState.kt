package org.mikhalchenkov.todoorbitmviexample.features.todo_list.state

import android.os.Parcelable
import org.mikhalchenkov.todoorbitmviexample.core.domain.entity.Todo

@Parcelize
data class TodoState (
    val todos: List<Todo> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val newTodoText: String = "",
    val filter: TodoFilter = TodoFilter.ALL
): Parcelable

enum class TodoFilter {
    ALL, ACTIVE, COMPLETED
}