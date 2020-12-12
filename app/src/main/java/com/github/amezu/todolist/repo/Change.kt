package com.github.amezu.todolist.repo

data class Change<T>(
    val item: T,
    val type: ChangeType
)
