package com.example.treeapp.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "node_closure",
    primaryKeys = ["ancestor_id", "descendant_id"],
    foreignKeys = [
        ForeignKey(
            entity = NodeEntity::class,
            parentColumns = ["id"],
            childColumns = ["ancestor_id"]
        ),
        ForeignKey(
            entity = NodeEntity::class,
            parentColumns = ["id"],
            childColumns = ["descendant_id"]
        )
    ]
)
data class NodeClosureEntity(
    @ColumnInfo(name = "ancestor_id")
    val parentId: Long,
    @ColumnInfo(name = "descendant_id")
    val childId: Long,
    @ColumnInfo(name = "depth")
    val depth: Int,
)