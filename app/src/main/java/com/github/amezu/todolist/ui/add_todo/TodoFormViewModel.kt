package com.github.amezu.todolist.ui.add_todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.amezu.todolist.model.Todo
import com.github.amezu.todolist.repo.TodosRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class TodoFormViewModel : ViewModel() {
    private val todoRepository = TodosRepository()
    private val disposables = CompositeDisposable()
    var todoToEdit: Todo? = null
    private val _operationResult = MutableLiveData<Throwable?>()
    val result: LiveData<Throwable?> = _operationResult

    fun doOnSaveClick(title: String, description: String) {
        todoRepository.run {
            todoToEdit?.let { update(it.copy(title = title, description = description)) }
                ?: create(Todo(title, description))
        }.subscribe(
            { _operationResult.value = null },
            { _operationResult.value = it }
        ).addTo(disposables)
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}