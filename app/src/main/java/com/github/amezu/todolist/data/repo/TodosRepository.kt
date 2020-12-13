package com.github.amezu.todolist.data.repo

import com.github.amezu.todolist.data.model.list.Change
import com.github.amezu.todolist.data.model.Todo
import io.reactivex.Completable
import io.reactivex.Observable

interface TodosRepository {
    fun create(todo: Todo): Completable
    fun delete(id: String): Completable
    fun getNextPage(): Observable<List<Change<Todo>>>?
    fun resetPages()
    fun update(todo: Todo): Completable
}