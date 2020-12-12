package com.github.amezu.todolist.ui.add_todo

import androidx.lifecycle.ViewModel
import com.github.amezu.todolist.model.Todo
import com.github.amezu.todolist.repo.TodosRepository
import io.reactivex.disposables.CompositeDisposable

class AddTodoViewModel : ViewModel() {
    private val todoRepository = TodosRepository()
    private val compositeDisposable = CompositeDisposable()

    fun doOnSaveClick(
        title: String,
        description: String,
        successHandler: () -> Unit,
        failureHandler: (Throwable) -> Unit
    ) {
        val disposable = todoRepository.save(Todo(title, description))
            .subscribe(successHandler, failureHandler)
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}