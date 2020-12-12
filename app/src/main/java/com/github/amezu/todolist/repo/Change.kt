package com.github.amezu.todolist.repo

import com.github.amezu.todolist.model.Todo

data class Change(
    val item: Todo,
    val type: ChangeType
)
