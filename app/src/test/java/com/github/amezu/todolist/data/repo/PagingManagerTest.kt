package com.github.amezu.todolist.data.repo

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.internal.stubbing.defaultanswers.TriesToReturnSelf
import kotlin.test.assertNotNull

internal class PagingManagerTest {

    private val query: Query = mock(defaultAnswer = TriesToReturnSelf())
    private val pagingManager = PagingManager(query, 2)
    private val documentSnapshot: DocumentSnapshot = mock()

    @Test
    fun `loads first page using initial query`() {
        reset(query)
        assertNotNull(pagingManager.getNextPage())
        verifyNoMoreInteractions(query)
    }

    @Nested
    inner class WhenFirstPageLoaded {

        private lateinit var firstPageListener: TodosChangesSubscription

        @BeforeEach
        internal fun setUp() {
            firstPageListener = pagingManager.getNextPage()!!
        }

        @Test
        fun `doesn't load next page when previous page not full`() {
            pagingManager.updatePagingParams(1, documentSnapshot, firstPageListener)
            assertNull(pagingManager.getNextPage())
        }

        @Test
        fun `loads next page starting after last item`() {
            pagingManager.updatePagingParams(2, documentSnapshot, firstPageListener)
            assertNotNull(pagingManager.getNextPage())
            verify(query).startAfter(documentSnapshot)
        }

        @Test
        fun `loads next page when last page was not full but item added`() {
            pagingManager.updatePagingParams(1, mock(), firstPageListener)
            pagingManager.updatePagingParams(2, documentSnapshot, firstPageListener)
            assertNotNull(pagingManager.getNextPage())
        }

        @Test
        fun `doesn't load next page when not last page was not full but item added`() {
            pagingManager.updatePagingParams(2, documentSnapshot, firstPageListener)
            val lastPageListener = pagingManager.getNextPage()!!
            pagingManager.updatePagingParams(1, documentSnapshot, lastPageListener)
            pagingManager.updatePagingParams(1, documentSnapshot, firstPageListener)
            pagingManager.updatePagingParams(2, documentSnapshot, firstPageListener)
        }
    }
}