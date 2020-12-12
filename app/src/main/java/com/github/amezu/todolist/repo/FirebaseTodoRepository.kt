package com.github.amezu.todolist.repo

import com.github.amezu.todolist.model.Todo
import com.google.common.base.Optional
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class FirebaseTodoRepository {
    val db = Firebase.firestore
    val collection = db.collection("todos")
    val pageSize = 30

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

    fun getPage(pageSize: Int, prevId: String? = null): Single<List<RealtimeTodo>> {
        return Single.create<List<RealtimeTodo>> { emitter ->
            var query: Query = collection.orderBy(Todo::createdDate.name).limit(pageSize.toLong())

            prevId?.let {
                query = Single.create<Query> { queryEmitter ->
                    collection.document(it).get()
                        .addOnSuccessListener { prevDocument ->
                            val prevTodo = prevDocument.toObject(Todo::class.java)
                                ?: throw IllegalArgumentException()
                            queryEmitter.onSuccess(query.startAfter(prevTodo))
                        }
                }.blockingGet()
            }

            query.get()
                .addOnSuccessListener {
                    emitter.onSuccess(
                        it?.documents?.map { RealtimeTodo(it.id, getTodo(it.id)) }!!
                    )
                }
        }
    }

    private fun getTodo(itemId: String): Observable<Optional<Todo>> =
        Observable.create<Optional<Todo>> { emitter ->
            collection.document(itemId)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        emitter.onError(exception)
                    } else if (snapshot != null && snapshot.exists()) {
                        emitter.onNext(
                            Optional.of(
                                snapshot.toObject(Todo::class.java)
                                    ?: throw IllegalArgumentException()
                            )
                        )
                    } else {
                        emitter.onNext(Optional.absent())
                    }
                }
        }
}