package com.example.treeapp.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.treeapp.domain.api.NodeDbInteractor
import com.example.treeapp.domain.model.Node
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val interactor: NodeDbInteractor,
) : ViewModel() {

    private val currentNodeId = MutableStateFlow(-1L)

    val currentNode = currentNodeId
        .map(interactor::getNodeById)
        .stateIn(viewModelScope, SharingStarted.Lazily, Node.create(null))


    @OptIn(ExperimentalCoroutinesApi::class)
    val children = currentNode
        .flatMapLatest { interactor.getChildren(it?.id ?: -1) }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    init {
        viewModelScope.launch {
            val rootNode = interactor.getRootNode() ?: run {
                interactor.addNodeToDb(Node.create(null), null)
                interactor.getRootNode()
            }
            rootNode?.let {
                currentNodeId.value = rootNode.id

            }
        }
    }

    fun goToTheNode(nodeId: Long) {
        currentNodeId.value = nodeId

    }

    fun goToTheParentNode() {
        currentNode.value?.isRoot?.let { isRoot ->
            if (!isRoot) {
                viewModelScope.launch {
                    val parentId = interactor.getParentId(currentNodeId.value)
                    parentId?.let {
                        goToTheNode(parentId)
                    }
                }
            }
        }
    }

    fun addChild() {
        val parentId = currentNodeId.value
        val node = Node.create(parentId = parentId)
        viewModelScope.launch {
            interactor.addNodeToDb(node, parentId)
        }
    }

    fun removeNode(nodeId: Long) {
        viewModelScope.launch {
            interactor.removeNodeFromDb(nodeId = nodeId)
        }
    }
}