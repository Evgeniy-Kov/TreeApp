package com.example.treeapp.di

import com.example.treeapp.ui.screen.MainScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MainScreenViewModel(get())
    }
}