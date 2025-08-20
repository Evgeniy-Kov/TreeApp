package com.example.treeapp.di

import com.example.treeapp.domain.api.NodeDbInteractor
import com.example.treeapp.domain.impl.NodeDbInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    single<NodeDbInteractor> {
        NodeDbInteractorImpl(get())
    }
}