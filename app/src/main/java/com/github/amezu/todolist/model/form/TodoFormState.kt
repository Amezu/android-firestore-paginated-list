package com.github.amezu.todolist.model.form

class TodoFormState(
    val titleError: Int? = null,
    val descriptionError: Int? = null,
    val iconUrlError: Int? = null
) {
    val isDataValid: Boolean
        get() = titleError == null && descriptionError == null && iconUrlError == null
}