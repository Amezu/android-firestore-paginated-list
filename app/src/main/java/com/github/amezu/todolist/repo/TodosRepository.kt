package com.github.amezu.todolist.repo

import com.github.amezu.todolist.model.Change
import com.github.amezu.todolist.model.Todo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.Completable
import io.reactivex.Observable

class TodosRepository {
    private val db = Firebase.firestore
    private val collectionPath = "todos"
    private val collection = db.collection(collectionPath)
    private var pagingManager = createPagingManager()

    private fun createPagingManager() = PagingManager(collection.orderBy(Todo::createdDate.name))

    fun create(todo: Todo): Completable {
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

    fun getNextPage(): Observable<List<Change<Todo>>>? {
        val pageSubscription = pagingManager.getNextPage() ?: return null
        return Observable.create<List<Change<Todo>>> { emitter ->
            pageSubscription.subscribe(object : TodosChangesSubscription.Observer {
                override fun handleChanges(changes: List<Change<Todo>>) {
                    emitter.onNext(changes)
                }
            })
        }.doOnDispose { pageSubscription.unsubscribe() }
    }

    fun resetPages() {
        pagingManager = createPagingManager()
    }

    fun update(todo: Todo): Completable {
        return Completable.create { emitter ->
            collection.document(todo.id).set(todo)
                .addOnFailureListener { emitter.onError(it) }
                .addOnSuccessListener { emitter.onComplete() }
        }
    }
}