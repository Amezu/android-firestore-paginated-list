package com.github.amezu.todolist.model.list

data class Change<T>(
    val item: T,
    val type: ChangeType
)
