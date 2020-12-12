package com.github.amezu.todolist.repo

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query

class PagingManager internal constructor(
    initialQuery: Query,
    private val pageSize: Int = 30
) {
    private var query = initialQuery.limit(pageSize.toLong())
    private var lastLoadedItem: DocumentSnapshot? = null
    private var isLastItemReached = false

    internal fun updatePagingParams(pageItemsCount: Int, lastLoadedItem: DocumentSnapshot?) {
        if (pageItemsCount < pageSize) {
            this.isLastItemReached = true
        } else {
            this.lastLoadedItem = lastLoadedItem
        }
    }

    fun getNextPage(): TodosLiveData? {
        if (isLastItemReached) {
            return null
        }

        lastLoadedItem?.let { query = query.startAfter(it) }

        return TodosLiveData(TodosChangesSubscription(query, this))
    }
}
