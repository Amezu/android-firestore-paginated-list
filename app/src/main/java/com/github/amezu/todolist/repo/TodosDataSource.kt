package com.github.amezu.todolist.repo

import android.annotation.SuppressLint
import androidx.paging.DataSource
import androidx.paging.ItemKeyedDataSource

class TodosDataSource(private val repository: FirebaseTodoRepository) :
    ItemKeyedDataSource<String, RealtimeTodo>() {

    @SuppressLint("CheckResult")
    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<RealtimeTodo>
    ) {
        repository.getPage(params.requestedLoadSize)
            .subscribe { page -> callback.onResult(page) }
    }

    @SuppressLint("CheckResult")
    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<RealtimeTodo>) {
        repository.getPage(params.requestedLoadSize, prevId = params.key)
            .subscribe { page -> callback.onResult(page) }
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<RealtimeTodo>) {
    }

    override fun getKey(item: RealtimeTodo) = item.id

    class Factory(private val repository: FirebaseTodoRepository) :
        DataSource.Factory<String, RealtimeTodo>() {

        override fun create(): DataSource<String, RealtimeTodo> {
            return TodosDataSource(repository)
        }
    }
}
