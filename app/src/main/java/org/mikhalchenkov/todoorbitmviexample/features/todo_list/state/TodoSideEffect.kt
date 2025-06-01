package org.mikhalchenkov.todoorbitmviexample.features.todo_list.state

sealed class TodoSideEffect {
    data class ShowMessage(val message: String) : TodoSideEffect()
}