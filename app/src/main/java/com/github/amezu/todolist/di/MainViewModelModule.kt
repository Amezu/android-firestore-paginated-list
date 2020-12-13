package com.github.amezu.todolist.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.amezu.todolist.ui.main.MainViewModel
import dagger.Binds
import dagger.Module

@Module
abstract class MainViewModelModule {

    @Binds
    abstract fun bindViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: MainViewModelFactory): ViewModelProvider.Factory
}
