package com.github.amezu.todolist.di

import com.github.amezu.todolist.ui.main.DeleteTodoDialogFragment
import com.github.amezu.todolist.ui.main.MainFragment
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
    fun inject(fragment: DeleteTodoDialogFragment)
}