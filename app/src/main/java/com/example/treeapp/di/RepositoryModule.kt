package com.example.treeapp.di

import com.example.treeapp.data.repository.NodeDbRepositoryImpl
import com.example.treeapp.domain.api.NodeDbRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<NodeDbRepository> {
        NodeDbRepositoryImpl(get())
    }
}