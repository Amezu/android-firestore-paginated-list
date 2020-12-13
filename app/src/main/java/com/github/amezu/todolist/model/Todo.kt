package com.github.amezu.todolist.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Todo(
    var title: String = "",
    var description: String = "",
    var iconUrl: String? = null,
    val createdDate: Date = Date(),
    @DocumentId val id: String = ""
) : Parcelable