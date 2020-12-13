package com.github.amezu.todolist.di

import com.github.amezu.todolist.ui.form.TodoFormFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        FirebaseTodosRepositoryModule::class,
        ViewModelModule::class
    ]
)
interface TodoFormFragmentComponent {

    fun inject(fragment: TodoFormFragment)
}