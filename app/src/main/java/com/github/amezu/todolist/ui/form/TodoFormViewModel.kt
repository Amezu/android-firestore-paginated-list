package com.github.amezu.todolist.ui.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.amezu.todolist.data.FormValidator
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
        updateFormState(title, description, iconUrl)
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

    private fun updateFormState(title: String, description: String, iconUrl: String) {
        _formState.value = FormValidator.validate(title, description, iconUrl)
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}