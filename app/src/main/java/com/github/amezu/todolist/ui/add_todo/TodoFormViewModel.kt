package com.github.amezu.todolist.ui.add_todo

import androidx.lifecycle.ViewModel
import com.github.amezu.todolist.model.Todo
import com.github.amezu.todolist.repo.TodosRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class TodoFormViewModel : ViewModel() {
    private val todoRepository = TodosRepository()
    private val disposables = CompositeDisposable()
    var todoToEdit: Todo? = null

    fun doOnSaveClick(
        title: String,
        description: String,
        successHandler: () -> Unit,
        failureHandler: (Throwable) -> Unit
    ) {
        todoRepository.run {
            todoToEdit?.let { update(it.copy(title = title, description = description)) }
                ?: create(Todo(title, description))
        }.subscribe(successHandler, failureHandler)
            .addTo(disposables)
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}