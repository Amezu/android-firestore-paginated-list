package com.github.amezu.todolist.repo

import android.util.Log
import com.github.amezu.todolist.model.Todo
import io.reactivex.Single

class TodosRepository {
    fun get(): Single<List<Todo>> {
        return Single.fromCallable {
            Log.d("MainViewModel", "single")
            listOf(
                Todo("title 1", "description"),
                Todo("title 2", "description"),
                Todo("title 3", "description")
            )
        }
    }
}