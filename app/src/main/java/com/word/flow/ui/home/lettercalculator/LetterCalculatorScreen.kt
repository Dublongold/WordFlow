package com.word.flow.ui.home.lettercalculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.word.flow.ui.home.lettercalculator.history.LetterCalculatorHistoryScreen
import com.word.flow.ui.navigation.LocalAppPadding
import com.word.flow.ui.navigation.LocalHistoryManager

@Composable
fun LetterCalculatorScreen(
    vm: LetterCalculatorViewModel = hiltViewModel(),
) {
    val state by vm.uiState.collectAsState()
    val historyManager = LocalHistoryManager.current
    if (historyManager.isHistoryNow) {
        LetterCalculatorHistoryScreen(onClick = {
            vm.setText(it)
            vm.proceedHistory()
            historyManager.leaveHistory()
        })
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(LocalAppPadding.current)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = state.text,
                        onValueChange = vm::setText,
                        label = { Text("Write content") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                    )
                    Button(
                        onClick = vm::proceed,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Proceed")
                    }
                }
            }

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Letters count: ${state.letterCount}")
                    Text("Letters in content:")
                    state.lettersFrequency.forEach { (letter, count) ->
                        Text("$letter - $count")
                    }
                }
            }
        }
    }
}
