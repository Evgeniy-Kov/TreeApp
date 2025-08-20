package com.example.treeapp

import android.app.Application
import com.example.treeapp.di.dataModule
import com.example.treeapp.di.interactorModule
import com.example.treeapp.di.repositoryModule
import com.example.treeapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, interactorModule, repositoryModule, viewModelModule)
        }
    }
}