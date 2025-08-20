package com.example.treeapp.domain.impl

import com.example.treeapp.domain.api.NodeDbInteractor
import com.example.treeapp.domain.api.NodeDbRepository
import com.example.treeapp.domain.model.Node
import kotlinx.coroutines.flow.Flow

class NodeDbInteractorImpl(
    private val repository: NodeDbRepository,
) : NodeDbInteractor {
    override suspend fun addNodeToDb(node: Node, parentId: Long?) {
        repository.addNodeToDb(node, parentId)
    }

    override suspend fun removeNodeFromDb(nodeId: Long) {
        repository.removeNodeFromDb(nodeId)
    }

    override suspend fun getRootNode(): Node? {
        return repository.getRootNode()
    }

    override suspend fun getNodeById(nodeId: Long): Node? {
        return repository.getNodeById(nodeId)
    }

    override suspend fun getParentId(nodeId: Long): Long? {
        return repository.getParentId(nodeId)
    }

    override fun getChildren(parentId: Long): Flow<List<Node>> {
        return repository.getChildren(parentId)
    }
}