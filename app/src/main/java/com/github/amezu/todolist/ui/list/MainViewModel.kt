package com.github.amezu.todolist.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.amezu.todolist.model.list.Change
import com.github.amezu.todolist.model.list.ChangeType
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

    private val _errors = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> = _errors

    fun loadNextPage() {
        todosRepository.getNextPage()?.let {
            _isLoadingNextPage.value = true
            it.doOnNext {
                _todos.applyChanges(it)
                _todosLiveData.value = _todos
                _isLoadingNextPage.value = false
            }.doOnError { _errors.value = it }
                .subscribe()
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

    fun doOnDeleteAccepted(id: String) {
        todosRepository.delete(id)
            .doOnError { _errors.value = it }
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