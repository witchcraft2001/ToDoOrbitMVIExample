package org.mikhalchenkov.todoorbitmviexample.features.todo_list.state

sealed class TodoSideEffect {
    data object ShowAddTodoSuccess : TodoSideEffect()
    data class ShowError(val message: String) : TodoSideEffect()
    data object HideKeyboard : TodoSideEffect()
    data class NavigateToTodoDetail(val todoId: String) : TodoSideEffect()
}