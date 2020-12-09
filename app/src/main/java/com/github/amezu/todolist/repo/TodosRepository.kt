package com.github.amezu.todolist.repo

import android.util.Log
import com.github.amezu.todolist.model.Todo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import io.reactivex.Single

class TodosRepository {
    private val db = Firebase.firestore
    private val collection = db.collection("todos")

    fun get(): Single<List<Todo>> {
        return Single.create<List<Todo>> { emitter ->
            collection.get()
                .addOnSuccessListener { querySnapshot ->
                    emitter.onSuccess(querySnapshot.toObjects())
                }.addOnFailureListener { error ->
                    emitter.onError(error)
                }
        }
    }
}