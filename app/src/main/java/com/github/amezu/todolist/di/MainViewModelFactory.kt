package com.github.amezu.todolist.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.amezu.todolist.repo.TodosRepository
import com.github.amezu.todolist.ui.main.MainViewModel
import javax.inject.Inject

class MainViewModelFactory @Inject constructor(
    private val todosRepository: TodosRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            MainViewModel(this.todosRepository) as T
        } else {
            throw IllegalArgumentException("MainViewModel not found")
        }
    }
}