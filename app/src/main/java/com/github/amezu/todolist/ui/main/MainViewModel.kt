package com.github.amezu.todolist.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.amezu.todolist.model.Change
import com.github.amezu.todolist.model.ChangeType
import com.github.amezu.todolist.model.Todo
import com.github.amezu.todolist.repo.TodosRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val todosRepository: TodosRepository
) : ViewModel() {
    private val disposables = CompositeDisposable()

    private val _isLoadingNextPage = MutableLiveData<Boolean>()
    val isLoadingNextPage: LiveData<Boolean> = _isLoadingNextPage

    private val _todos = mutableListOf<Todo>()
    private val _todosLiveData = MutableLiveData<List<Todo>>()
    val todos: LiveData<List<Todo>> = _todosLiveData

    fun loadNextPage() {
        todosRepository.getNextPage()?.let {
            _isLoadingNextPage.value = true
            it.doOnError(Throwable::printStackTrace)
                .doOnNext {
                    _todos.applyChanges(it)
                    _todosLiveData.value = _todos
                    _isLoadingNextPage.value = false
                }.subscribe()
                .addTo(disposables)
        }
    }

    private fun MutableList<Todo>.applyChanges(changes: List<Change<Todo>>) {
        changes.forEach { change ->
            when (change.type) {
                ChangeType.ADDED -> add(change.item)
                ChangeType.MODIFIED -> {
                    val index = findItemIndex(change)
                    index?.let { removeAt(it) }
                    add(index ?: size, change.item)
                }
                ChangeType.REMOVED -> {
                    val index = findItemIndex(change)
                    index?.let { removeAt(it) }
                }
            }
        }
    }

    private fun List<Todo>.findItemIndex(change: Change<Todo>): Int? {
        return indexOfFirst { it.id == change.item.id }.takeUnless { it < 0 }
    }

    fun delete(id: String, errorHandler: (Throwable) -> Unit) {
        todosRepository.delete(id)
            .doOnError(errorHandler)
            .subscribe()
            .addTo(disposables)
    }

    override fun onCleared() {
        disposables.dispose()
        todosRepository.resetPages()
        _todos.clear()
        _isLoadingNextPage.value = false
        super.onCleared()
    }
}