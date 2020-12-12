package com.github.amezu.todolist.model

data class Change<T>(
    val item: T,
    val type: ChangeType
)
