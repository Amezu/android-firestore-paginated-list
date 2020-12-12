package com.github.amezu.todolist.repo

import com.github.amezu.todolist.model.Todo
import com.google.common.base.Optional
import io.reactivex.Observable

data class RealtimeTodo(
    val id: String,
    val todo: Observable<Optional<Todo>>
)