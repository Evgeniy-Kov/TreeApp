package com.example.treeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import com.example.treeapp.ui.screen.NodeScreen
import com.example.treeapp.ui.theme.TreeAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TreeAppTheme {
                Box(modifier = Modifier.systemBarsPadding()) {
                    NodeScreen()
                }
            }
        }
    }
}