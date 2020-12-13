package com.github.amezu.todolist.ui.form

import androidx.lifecycle.Observer
import com.github.amezu.todolist.InstantExecutorExtension
import com.github.amezu.todolist.data.model.Todo
import com.github.amezu.todolist.data.repo.TodosRepository
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Completable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.internal.stubbing.defaultanswers.ReturnsMocks
import kotlin.test.assertEquals
import kotlin.test.assertNotSame
import kotlin.test.assertNull

@ExtendWith(InstantExecutorExtension::class)
class TodoFormViewModelTest {

    private val todosRepository: TodosRepository = mock(defaultAnswer = ReturnsMocks())
    private val underTest = TodoFormViewModel(todosRepository)

    private val resultObserver: Observer<in Throwable?> = mock()

    @BeforeEach
    internal fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun `creates todo when valid data`() {
        underTest.doOnSaveClick(validTitle, validDescription, validIconUrl)

        argumentCaptor<Todo>().apply {
            verify(todosRepository, only()).create(capture())

            assertEquals(validTitle, firstValue.title)
            assertEquals(validDescription, firstValue.description)
            assertEquals(validIconUrl, firstValue.iconUrl)
        }
    }

    @Test
    internal fun `notifies when todo successfully created`() {
        underTest.result.observeForever(resultObserver)
        whenever(todosRepository.create(any())).thenReturn(Completable.complete())
        underTest.doOnSaveClick(validTitle, validDescription, validIconUrl)

        argumentCaptor<Throwable>().apply {
            verify(resultObserver, only()).onChanged(capture())

            assertNull(firstValue)
        }
    }

    @Test
    internal fun `notifies error occuring during todo creating`() {
        underTest.result.observeForever(resultObserver)
        val throwable = Throwable()
        whenever(todosRepository.create(any())).thenReturn(Completable.error(throwable))
        underTest.doOnSaveClick(validTitle, validDescription, validIconUrl)

        argumentCaptor<Throwable>().apply {
            verify(resultObserver, only()).onChanged(capture())

            assertEquals(throwable, firstValue)
        }
    }

    @Test
    fun `updates todo when valid data and selected todo`() {
        val selectedTodo = Todo(id = "id")
        underTest.selectedTodo = selectedTodo
        underTest.doOnSaveClick(validTitle, validDescription, validIconUrl)

        argumentCaptor<Todo>().apply {
            verify(todosRepository, only()).update(capture())

            assertEquals(validTitle, firstValue.title)
            assertEquals(validDescription, firstValue.description)
            assertEquals(validIconUrl, firstValue.iconUrl)
            assertEquals(selectedTodo.createdDate, firstValue.createdDate)
            assertEquals(selectedTodo.id, firstValue.id)
            assertNotSame(selectedTodo, firstValue)
        }
    }

    @Test
    internal fun `does nothing when invalid data`() {
        underTest.doOnSaveClick(invalidTitle, validDescription, validIconUrl)
        verifyNoMoreInteractions(todosRepository)
    }

    companion object {
        private const val validTitle = "title"
        private const val validDescription = "description"
        private const val validIconUrl = ""
        private const val invalidTitle = ""
    }
}