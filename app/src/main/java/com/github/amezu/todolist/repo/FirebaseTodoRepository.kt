package com.github.amezu.todolist.repo

import com.github.amezu.todolist.model.Todo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.Completable

class FirebaseTodoRepository {
    val db = Firebase.firestore
    val collection = db.collection("todos")

    fun save(todo: Todo): Completable {
        return Completable.create { emitter ->
            collection.add(todo)
                .addOnFailureListener { emitter.onError(it) }
                .addOnSuccessListener { emitter.onComplete() }
        }
    }

    fun delete(todo: Todo): Completable {
        return Completable.create { emitter ->
            collection.document(todo.id).delete()
                .addOnFailureListener { emitter.onError(it) }
                .addOnSuccessListener { emitter.onComplete() }
        }
    }
}