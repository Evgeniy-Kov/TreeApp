package com.example.treeapp.ui.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.treeapp.domain.model.Node

@Composable
fun BottomButtons(
    node: Node?,
    onAddClicked: () -> Unit,
    onGoToParentClicked: () -> Unit,
) {
    Row(modifier = Modifier.padding(8.dp)) {
        Button(
            modifier = Modifier.weight(1f),
            enabled = node?.isRoot == false,
            onClick = {
                onGoToParentClicked()
            }
        ) {
            Text(text = "Go to parent")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            modifier = Modifier.weight(1f),
            onClick = {
                onAddClicked()
            }
        ) {
            Text(text = "Add new child")
        }
    }
}