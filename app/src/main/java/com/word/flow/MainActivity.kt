package com.word.flow

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.word.flow.ui.navigation.AppNavGraph
import com.word.flow.ui.theme.WordFlowTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val systemBarStyle = SystemBarStyle.dark(
        Color.TRANSPARENT
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(systemBarStyle, systemBarStyle)
        setContent {
            WordFlowTheme {
                AppNavGraph()
            }
        }
    }
}
