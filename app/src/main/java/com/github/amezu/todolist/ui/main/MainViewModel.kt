package com.github.amezu.todolist.ui.main

import androidx.lifecycle.ViewModel
import com.github.amezu.todolist.repo.TodosRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class MainViewModel : ViewModel() {
    private val todoRepository = TodosRepository()
    private val disposables = CompositeDisposable()

    fun getNextPage() = todoRepository.getNextPage()

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