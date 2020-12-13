package com.github.amezu.todolist.ui.todo_form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.amezu.todolist.model.Todo
import com.github.amezu.todolist.repo.FirebaseTodosRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class TodoFormViewModel : ViewModel() {
    private val todoRepository = FirebaseTodosRepository()
    private val disposables = CompositeDisposable()
    var todoToEdit: Todo? = null
    private val _result = MutableLiveData<Throwable?>()
    val result: LiveData<Throwable?> = _result

    fun doOnSaveClick(title: String, description: String) {
        todoRepository.run {
            todoToEdit?.let { update(it.copy(title = title, description = description)) }
                ?: create(Todo(title, description))
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