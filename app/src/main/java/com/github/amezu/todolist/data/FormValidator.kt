package com.github.amezu.todolist.data

import android.util.Patterns
import com.github.amezu.todolist.R
import com.github.amezu.todolist.data.model.form.TodoFormState

object FormValidator {
    fun validate(title: String, description: String, iconUrl: String): TodoFormState {
        return TodoFormState(
            validateTitle(title),
            validateDescription(description),
            validateIconUrl(iconUrl)
        )
    }

    private fun validateTitle(title: String): Int? {
        return when {
            title.isEmpty() -> R.string.invalid_title_empty
            title.length > 30 -> R.string.invalid_title_too_long
            else -> null
        }
    }

    private fun validateDescription(description: String): Int? {
        return when {
            description.length > 200 -> R.string.invalid_description_too_long
            else -> null
        }
    }

    private fun validateIconUrl(iconUrl: String): Int? {
        return when {
            iconUrl.isEmpty() -> null
            isNotValidUrl(
                iconUrl
            ) -> R.string.invalid_url
            else -> null
        }
    }

    private fun isNotValidUrl(iconUrl: String) = !Patterns.WEB_URL.matcher(iconUrl).matches()
}