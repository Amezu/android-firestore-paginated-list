package com.github.amezu.todolist.repo

import com.github.amezu.todolist.model.Change
import com.github.amezu.todolist.model.Todo
import io.reactivex.Completable
import io.reactivex.Observable

interface TodosRepository {
    fun create(todo: Todo): Completable
    fun delete(id: String): Completable
    fun getNextPage(): Observable<List<Change<Todo>>>?
    fun resetPages()
    fun update(todo: Todo): Completable
}