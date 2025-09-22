package org.mikhalchenkov.todoorbitmviexample.features.todo_list.state

import android.os.Parcelable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.parcelize.Parcelize
import org.mikhalchenkov.todoorbitmviexample.core.domain.entity.Todo

@Parcelize
data class TodoState (
    val todos: ImmutableList<Todo> = persistentListOf(),
    val isLoading: Boolean = false,
    val error: String? = null,
): Parcelable
