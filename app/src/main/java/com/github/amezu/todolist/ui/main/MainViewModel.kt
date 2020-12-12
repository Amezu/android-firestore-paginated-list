package com.github.amezu.todolist.ui.main

import androidx.lifecycle.ViewModel
import com.github.amezu.todolist.repo.FirebaseTodoRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class MainViewModel : ViewModel() {
    private val todoRepository = FirebaseTodoRepository()
    private val disposables = CompositeDisposable()

    fun getChanges() = todoRepository.getChanges()

    fun delete(id: String, errorHandler: (Throwable) -> Unit) {
        todoRepository.delete(id)
            .doOnError(errorHandler)
            .subscribe()
            .addTo(disposables)
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}