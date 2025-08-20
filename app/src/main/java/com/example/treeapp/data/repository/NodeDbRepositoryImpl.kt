package com.example.treeapp.data.repository

import com.example.treeapp.data.db.AppDatabase
import com.example.treeapp.data.db.mappers.toNode
import com.example.treeapp.data.db.mappers.toNodeEntity
import com.example.treeapp.domain.api.NodeDbRepository
import com.example.treeapp.domain.model.Node
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NodeDbRepositoryImpl(
    private val appDb: AppDatabase,
) : NodeDbRepository {
    override suspend fun addNodeToDb(node: Node, parentId: Long?) {
        appDb.nodeDao().insertNodeWithClosure(node.toNodeEntity(), parentId)
    }

    override suspend fun removeNodeFromDb(nodeId: Long) {
        appDb.nodeDao().deleteNodeAndClosure(nodeId)
    }

    override suspend fun getRootNode(): Node? {
        return appDb.nodeDao().getRootNode()?.toNode()
    }

    override suspend fun getNodeById(nodeId: Long): Node? {
        return appDb.nodeDao().getNodeById(nodeId)?.toNode()
    }

    override suspend fun getParentId(nodeId: Long): Long? {
        return appDb.nodeDao().getParentId(nodeId)
    }

    override fun getChildren(parentId: Long): Flow<List<Node>> {
        return appDb.nodeDao().getChildren(parentId).map {
            it.map { nodeEntity -> nodeEntity.toNode() }
        }
    }
}