package com.example.treeapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.treeapp.data.db.entity.NodeClosureEntity
import com.example.treeapp.data.db.entity.NodeEntity

@Database(
    version = 1,
    entities = [
        NodeEntity::class,
        NodeClosureEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun nodeDao(): NodeDao
}