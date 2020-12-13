package com.github.amezu.todolist.ui.form

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.amezu.todolist.R
import com.github.amezu.todolist.data.model.Todo
import com.github.amezu.todolist.data.model.form.TodoFormState
import com.github.amezu.todolist.data.repo.TodosRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TodoFormViewModel @Inject constructor(
    private val todosRepository: TodosRepository
) : ViewModel() {
    private val disposables = CompositeDisposable()

    private val _formState = MutableLiveData<TodoFormState>()
    val formState: LiveData<TodoFormState> = _formState

    var selectedTodo: Todo? = null

    private val _result = MutableLiveData<Throwable?>()
    val result: LiveData<Throwable?> = _result

    fun doOnSaveClick(title: String, description: String, iconUrl: String) {
        validate(title, description, iconUrl)
        if (formState.value?.isDataValid != true) return

        todosRepository.run {
            selectedTodo?.let { update(it.copy(title, description, iconUrl)) }
                ?: create(Todo(title, description, iconUrl))
        }.subscribeOn(Schedulers.io())
            .subscribe(
            { _result.value = null },
            { _result.value = it }
        ).addTo(disposables)
    }

    private fun validate(title: String, description: String, iconUrl: String) {
        _formState.value = TodoFormState(
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
            isNotValidUrl(iconUrl) -> R.string.invalid_url
            else -> null
        }
    }

    private fun isNotValidUrl(iconUrl: String) = !Patterns.WEB_URL.matcher(iconUrl).matches()

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}