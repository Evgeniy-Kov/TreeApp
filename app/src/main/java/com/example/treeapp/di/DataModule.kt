package com.example.treeapp.di

import androidx.room.Room
import com.example.treeapp.data.db.AppDatabase
import org.koin.dsl.module

val dataModule = module {
    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "app_database")
            .build()
    }
}