package com.github.amezu.todolist.ui.main

import androidx.lifecycle.ViewModel
import com.github.amezu.todolist.model.Todo
import com.github.amezu.todolist.repo.FirebaseTodoRepository
import io.reactivex.disposables.CompositeDisposable

class MainViewModel : ViewModel() {
    private val todoRepository = FirebaseTodoRepository()
    private val compositeDisposable = CompositeDisposable()

    fun delete(
        todo: Todo,
        errorHandler: (Throwable) -> Unit,
        successHandler: () -> Unit
    ) {
        val disposable = todoRepository.delete(todo)
            .subscribe(successHandler, errorHandler)
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}