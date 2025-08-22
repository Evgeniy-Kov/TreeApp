package com.example.treeapp.data.repository

import com.example.treeapp.data.db.AppDatabase
import com.example.treeapp.data.db.mappers.toNode
import com.example.treeapp.data.db.mappers.toNodeEntity
import com.example.treeapp.domain.api.NodeDbRepository
import com.example.treeapp.domain.model.Node
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class NodeDbRepositoryImpl(
    private val appDb: AppDatabase,
) : NodeDbRepository {
    override suspend fun addNodeToDb(node: Node, parentId: Long?) {
        withContext(Dispatchers.IO) {
            appDb.nodeDao().insertNodeWithClosure(node.toNodeEntity(), parentId)
        }
    }

    override suspend fun removeNodeFromDb(nodeId: Long) {
        withContext(Dispatchers.IO) {
            appDb.nodeDao().deleteNodeAndClosure(nodeId)
        }
    }

    override suspend fun getRootNode(): Node? {
        return withContext(Dispatchers.IO) {
            appDb.nodeDao().getRootNode()?.toNode()
        }
    }

    override suspend fun getNodeById(nodeId: Long): Node? {
        return withContext(Dispatchers.IO) {
            appDb.nodeDao().getNodeById(nodeId)?.toNode()
        }
    }

    override suspend fun getParentId(nodeId: Long): Long? {
        return withContext(Dispatchers.IO) {
            appDb.nodeDao().getParentId(nodeId)
        }
    }

    override fun getChildren(parentId: Long): Flow<List<Node>> {
        return appDb.nodeDao().getChildren(parentId).map {
            it.map { nodeEntity -> nodeEntity.toNode() }
        }.flowOn(Dispatchers.IO)
    }
}