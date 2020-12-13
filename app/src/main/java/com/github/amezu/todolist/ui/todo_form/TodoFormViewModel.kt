package com.github.amezu.todolist.ui.todo_form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.amezu.todolist.model.Todo
import com.github.amezu.todolist.repo.TodosRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class TodoFormViewModel @Inject constructor(
    private val todosRepository: TodosRepository
) : ViewModel() {
    private val disposables = CompositeDisposable()

    var selectedTodo: Todo? = null

    private val _result = MutableLiveData<Throwable?>()
    val result: LiveData<Throwable?> = _result

    fun doOnSaveClick(title: String, description: String, iconUrl: String) {
        todosRepository.run {
            selectedTodo?.let { update(it.copy(title, description, iconUrl)) }
                ?: create(Todo(title, description, iconUrl))
        }.subscribe(
            { _result.value = null },
            { _result.value = it }
        ).addTo(disposables)
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}