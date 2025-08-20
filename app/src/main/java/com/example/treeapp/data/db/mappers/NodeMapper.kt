package com.example.treeapp.data.db.mappers

import com.example.treeapp.data.db.entity.NodeEntity
import com.example.treeapp.domain.model.Node

fun Node.toNodeEntity() = NodeEntity(
    id = this.id,
    name = this.name,
    isRoot = this.isRoot
)

fun NodeEntity.toNode() = Node(
    id = this.id,
    name = this.name,
    isRoot = this.isRoot
)