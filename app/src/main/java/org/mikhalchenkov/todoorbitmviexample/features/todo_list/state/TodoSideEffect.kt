package org.mikhalchenkov.todoorbitmviexample.features.todo_list.state

sealed interface TodoSideEffect {
    data class ShowMessage(val message: String) : TodoSideEffect
}