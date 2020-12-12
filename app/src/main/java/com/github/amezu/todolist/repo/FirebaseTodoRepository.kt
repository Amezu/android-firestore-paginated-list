package com.github.amezu.todolist.repo

import com.github.amezu.todolist.model.Todo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.Completable

class FirebaseTodoRepository {
    private val db = Firebase.firestore
    private val collectionPath = "todos"
    private val collection = db.collection(collectionPath)
    private val pagingManager = PagingManager(collection.orderBy(Todo::createdDate.name))

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

    fun getNextPage(): TodosLiveData? {
        return pagingManager.getNextPage()
    }
}