package com.github.amezu.todolist.di

import com.github.amezu.todolist.ui.list.TodoDeletionDialogFragment
import com.github.amezu.todolist.ui.list.MainFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        FirebaseTodosRepositoryModule::class,
        ViewModelModule::class
    ]
)
interface MainFragmentComponent {

    fun inject(fragment: MainFragment)
    fun inject(fragment: TodoDeletionDialogFragment)
}