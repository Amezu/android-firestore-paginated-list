package com.github.amezu.todolist.model

import com.google.firebase.firestore.DocumentId
import java.util.*

data class Todo(
    val title: String = "",
    val description: String = "",
    val iconUrl: String? = null,
    val createdDate: Date = Date(),
    @DocumentId val id: String = ""
)