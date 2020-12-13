package com.github.amezu.todolist.di

import com.github.amezu.todolist.data.repo.FirebaseTodosRepository
import com.github.amezu.todolist.data.repo.TodosRepository
import dagger.Binds
import dagger.Module

@Module
abstract class FirebaseTodosRepositoryModule {

    @Binds
    abstract fun bindTodosRepository(todosRepository: FirebaseTodosRepository): TodosRepository
}
