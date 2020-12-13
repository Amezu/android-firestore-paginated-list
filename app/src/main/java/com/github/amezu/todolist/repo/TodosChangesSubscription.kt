package com.github.amezu.todolist.repo

import com.github.amezu.todolist.model.Change
import com.github.amezu.todolist.model.ChangeType
import com.github.amezu.todolist.model.Todo
import com.google.firebase.firestore.*

internal class TodosChangesSubscription(
    private val query: Query,
    private val pagingManager: PagingManager
) : EventListener<QuerySnapshot?> {
    private var listenerRegistration: ListenerRegistration? = null
    private lateinit var observer: Observer

    fun subscribe(observer: Observer) {
        listenerRegistration = query.addSnapshotListener(this)
        this.observer = observer
    }

    fun unsubscribe() {
        listenerRegistration?.remove()
    }

    override fun onEvent(querySnapshot: QuerySnapshot?, error: FirebaseFirestoreException?) {
        if (error != null || querySnapshot == null) return
        val changes = querySnapshot.documentChanges.map {
            val changedItem: Todo = it.document.toObject(Todo::class.java)
            when (it.type) {
                DocumentChange.Type.ADDED -> Change(
                    changedItem,
                    ChangeType.ADDED
                )
                DocumentChange.Type.MODIFIED -> Change(
                    changedItem,
                    ChangeType.MODIFIED
                )
                DocumentChange.Type.REMOVED -> Change(
                    changedItem,
                    ChangeType.REMOVED
                )
            }
        }
        val queryItemsCount = querySnapshot.size()
        val lastItem = (queryItemsCount - 1).takeUnless { it < 0 }?.let {
            querySnapshot.documents[it]
        }

        observer.handleChanges(changes)
        pagingManager.updatePagingParams(queryItemsCount, lastItem, this)
    }

    interface Observer {
        fun handleChanges(changes: List<Change<Todo>>)
    }
}