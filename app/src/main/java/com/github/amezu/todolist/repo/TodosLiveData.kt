package com.github.amezu.todolist.repo

import androidx.lifecycle.LiveData
import com.github.amezu.todolist.model.Todo


class TodosLiveData internal constructor(
    private val changesSubscription: TodosChangesSubscription
) : LiveData<Change<Todo>>(), TodosChangesSubscription.Observer {

    override fun onActive() {
        changesSubscription.subscribe(this)
    }

    override fun onInactive() {
        changesSubscription.unsubscribe()
    }

    override fun handleChanges(changes: List<Change<Todo>>) {
        changes.forEach { value = it }
    }
}