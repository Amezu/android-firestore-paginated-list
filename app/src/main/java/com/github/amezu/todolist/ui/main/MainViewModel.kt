package com.github.amezu.todolist.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.github.amezu.todolist.model.Todo
import com.github.amezu.todolist.repo.FirebaseTodoRepository
import com.github.amezu.todolist.repo.RealtimeTodo
import com.github.amezu.todolist.repo.TodosDataSource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class MainViewModel : ViewModel() {
    private val todoRepository = FirebaseTodoRepository()
    private val disposables = CompositeDisposable()

    private val config = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setInitialLoadSizeHint(30)
        .setPageSize(30)
        .build()
    val todos: LiveData<PagedList<RealtimeTodo>> =
        LivePagedListBuilder<String, RealtimeTodo>(
            TodosDataSource.Factory(todoRepository),
            config
        ).build()

    fun delete(
        todo: Todo,
        errorHandler: (Throwable) -> Unit
    ) {
        todoRepository.delete(todo)
            .doOnError(errorHandler)
            .subscribe()
            .addTo(disposables)
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}