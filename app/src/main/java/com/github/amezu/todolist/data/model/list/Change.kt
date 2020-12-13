package com.github.amezu.todolist.data.model.list

data class Change<T>(
    val item: T,
    val type: ChangeType
)
