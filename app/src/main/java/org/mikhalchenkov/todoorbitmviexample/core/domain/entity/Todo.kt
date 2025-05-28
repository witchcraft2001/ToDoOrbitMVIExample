package org.mikhalchenkov.todoorbitmviexample.core.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Todo(
    val id: Long,
    val text: String,
    val isCompleted: Boolean = false,
) : Parcelable
