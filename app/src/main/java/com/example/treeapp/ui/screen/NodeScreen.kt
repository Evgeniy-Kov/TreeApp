package com.example.treeapp.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun NodeScreen(
    modifier: Modifier = Modifier,
) {

    val viewModel = koinViewModel<MainScreenViewModel>()

    val parentState by viewModel.currentNode.collectAsStateWithLifecycle()

    val childrenState by viewModel.children.collectAsStateWithLifecycle()

    BackHandler {
        viewModel.goToTheParentNode()
    }

    Scaffold(
        bottomBar = {
            BottomButtons(
                node = parentState,
                onAddClicked = { viewModel.addChild() },
                onGoToParentClicked = {
                    viewModel.goToTheParentNode()
                }
            )
        }
    )
    { innerPaddings ->
        Column(
            modifier = modifier
                .padding(innerPaddings)
                .padding(8.dp)
        ) {

            NodeItem(
                node = parentState
            )

            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(items = childrenState) { item ->
                    ChildNodeItem(
                        node = item,
                        onItemClickListener = {
                            viewModel.goToTheNode(nodeId = item.id)
                        },
                        onRemoveClick = {
                            viewModel.removeNode(it)
                        }
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun NodeScreenPreview() {
    NodeScreen()
}