package com.github.amezu.todolist.ui.main

import androidx.lifecycle.ViewModel
import com.github.amezu.todolist.model.Todo
import com.github.amezu.todolist.repo.TodosRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel : ViewModel() {
    private var todosRepository = TodosRepository()
    private var compositeDisposable = CompositeDisposable()

    fun loadTodos(): Single<List<Todo>> {
        return todosRepository.get()
            .doOnSubscribe { compositeDisposable.add(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}