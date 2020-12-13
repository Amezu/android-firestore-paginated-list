package com.github.amezu.todolist.di

import com.github.amezu.todolist.ui.list.DeleteTodoDialogFragment
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
    fun inject(fragment: DeleteTodoDialogFragment)
}