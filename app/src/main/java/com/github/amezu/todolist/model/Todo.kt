package com.github.amezu.todolist.model

import java.util.*
import kotlin.random.Random

data class Todo(
    val title: String = "",
    val description: String = "",
    val iconUrl: String? = null,
    val createdDate: Date = Date(),
    val id: Long = Random.nextLong()
)