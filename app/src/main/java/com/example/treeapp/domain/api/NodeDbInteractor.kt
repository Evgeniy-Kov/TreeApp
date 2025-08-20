package com.example.treeapp.domain.api

import com.example.treeapp.domain.model.Node
import kotlinx.coroutines.flow.Flow

interface NodeDbInteractor {

    suspend fun addNodeToDb(node: Node, parentId: Long?)

    suspend fun removeNodeFromDb(nodeId: Long)

    suspend fun getRootNode(): Node?

    suspend fun getNodeById(nodeId: Long): Node?

    suspend fun getParentId(nodeId: Long): Long?

    fun getChildren(parentId: Long): Flow<List<Node>>
}