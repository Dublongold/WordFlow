package com.word.flow.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

class HistoryManager {
    var isHistoryNow by mutableStateOf(false)
        private set
    fun enterHistory() {
        isHistoryNow = true
    }
    fun leaveHistory() {
        isHistoryNow = false
    }
    fun leaveHistory(ifNot: () -> Unit) {
        if (isHistoryNow) {
            isHistoryNow = false
        } else ifNot()
    }

    @Composable
    fun BackHandler() {
        BackHandler(enabled = isHistoryNow) {
            leaveHistory()
        }
    }
}

val LocalHistoryManager = staticCompositionLocalOf {
    HistoryManager()
}