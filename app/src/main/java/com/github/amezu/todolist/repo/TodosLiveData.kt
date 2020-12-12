package com.github.amezu.todolist.repo

import androidx.lifecycle.LiveData
import com.github.amezu.todolist.model.Todo
import com.google.firebase.firestore.*


class TodosLiveData internal constructor(
    private val query: Query,
    private val pageSize: Int,
    private val onLastVisibleTodoCallback: OnLastVisibleTodoCallback,
    private val onLastTodoReachedCallback: OnLastTodoReachedCallback
) : LiveData<Change>(), EventListener<QuerySnapshot?> {
    private var listenerRegistration: ListenerRegistration? = null

    override fun onActive() {
        listenerRegistration = query.addSnapshotListener(this)
    }

    override fun onInactive() {
        listenerRegistration?.remove()
    }

    override fun onEvent(querySnapshot: QuerySnapshot?, error: FirebaseFirestoreException?) {
        if (error != null || querySnapshot == null) return
        querySnapshot.documentChanges.forEach {
            val changedItem: Todo = it.document.toObject(Todo::class.java)
            value = when (it.type) {
                DocumentChange.Type.ADDED -> Change(changedItem, ChangeType.ADDED)
                DocumentChange.Type.MODIFIED -> Change(changedItem, ChangeType.MODIFIED)
                DocumentChange.Type.REMOVED -> Change(changedItem, ChangeType.REMOVED)
            }
        }
        val querySnapshotSize = querySnapshot.size()
        if (querySnapshotSize < pageSize) {
            onLastTodoReachedCallback.setLastTodoReached(true)
        } else {
            val lastVisibleTodo =
                querySnapshot.documents[querySnapshotSize - 1]
            onLastVisibleTodoCallback.setLastVisibleTodo(lastVisibleTodo)
        }
    }

    internal interface OnLastVisibleTodoCallback {
        fun setLastVisibleTodo(lastVisibleTodo: DocumentSnapshot?)
    }

    internal interface OnLastTodoReachedCallback {
        fun setLastTodoReached(isLastTodoReached: Boolean)
    }
}