package com.github.amezu.todolist.repo

import com.github.amezu.todolist.model.Todo
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.Completable

class FirebaseTodoRepository {
    private val db = Firebase.firestore
    private val collection = db.collection("todos")
    private val pageSize = 30
    private var query = collection.orderBy(Todo::createdDate.name).limit(pageSize.toLong())
    private var lastVisibleItem: DocumentSnapshot? = null
    private var isLastItemReached = false

    fun save(todo: Todo): Completable {
        return Completable.create { emitter ->
            collection.add(todo)
                .addOnFailureListener { emitter.onError(it) }
                .addOnSuccessListener { emitter.onComplete() }
        }
    }

    fun delete(id: String): Completable {
        return Completable.create { emitter ->
            collection.document(id).delete()
                .addOnFailureListener { emitter.onError(it) }
                .addOnSuccessListener { emitter.onComplete() }
        }
    }

    fun getChanges(): TodosLiveData? {
        if (isLastItemReached) {
            return null
        }

        lastVisibleItem?.let { query = query.startAfter(it) }

        return TodosLiveData(query, pageSize,
            object : TodosLiveData.OnLastVisibleTodoCallback {
                override fun setLastVisibleTodo(lastVisibleTodo: DocumentSnapshot?) {
                    lastVisibleItem = lastVisibleTodo
                }
            }, object : TodosLiveData.OnLastTodoReachedCallback {
                override fun setLastTodoReached(isLastTodoReached: Boolean) {
                    isLastItemReached = isLastTodoReached
                }
            })
    }
}