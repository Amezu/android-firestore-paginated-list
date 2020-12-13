package com.github.amezu.todolist.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.amezu.todolist.model.Change
import com.github.amezu.todolist.model.Todo
import com.github.amezu.todolist.repo.TodosRepository
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class MainViewModel : ViewModel() {
    private val todoRepository = TodosRepository()
    private val disposables = CompositeDisposable()

    private val _isLoadingNextPage = MutableLiveData<Boolean>()
    val isLoadingNextPage: LiveData<Boolean> = _isLoadingNextPage

    fun loadNextPage(): Observable<List<Change<Todo>>>? {
        _isLoadingNextPage.value = true
        return todoRepository.getNextPage()
            ?.doOnError(Throwable::printStackTrace)
            ?.doOnSubscribe { it.addTo(disposables) }
    }

    fun doOnPageLoaded() {
        _isLoadingNextPage.value = false
    }

    fun delete(id: String, errorHandler: (Throwable) -> Unit) {
        todoRepository.delete(id)
            .doOnError(errorHandler)
            .subscribe()
            .addTo(disposables)
    }

    override fun onCleared() {
        disposables.dispose()
        todoRepository.resetPages()
        super.onCleared()
    }
}