package com.github.amezu.todolist.data.repo

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query

internal class PagingManager internal constructor(
    initialQuery: Query,
    private val pageSize: Int = 30
) {
    private var query = initialQuery.limit(pageSize.toLong())
    private var lastLoadedItem: DocumentSnapshot? = null
    private var isLastItemReached = false

    private var lastSubscription: TodosChangesSubscription? = null
    private var previousLastPageItemsCount = 0

    internal fun updatePagingParams(
        pageItemsCount: Int,
        lastLoadedItem: DocumentSnapshot?,
        subscription: TodosChangesSubscription
    ) {
        if (subscription == lastSubscription) {
            this.lastLoadedItem = lastLoadedItem
            updateIsLastItemReached(pageItemsCount)
            previousLastPageItemsCount = pageItemsCount
        }
    }

    private fun updateIsLastItemReached(newLastPageItemsCount: Int) {
        if (newLastPageItemsCount < pageSize) {
            isLastItemReached = true
        } else if (previousLastPageItemsCount < newLastPageItemsCount) {
            isLastItemReached = false
        }
    }

    fun getNextPage(): TodosChangesSubscription? {
        if (isLastItemReached) {
            return null
        }

        lastLoadedItem?.let { query = query.startAfter(it) }

        return TodosChangesSubscription(query, this).also { lastSubscription = it }
    }
}
