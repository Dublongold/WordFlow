package com.word.flow.ui.home.wordscounter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.word.flow.data.local.db.dao.fake.FakeCountersDao
import com.word.flow.domain.usecase.CountWordsUseCase
import com.word.flow.domain.usecase.GetCountersUseCase
import com.word.flow.domain.usecase.UpsertCountersUseCase
import com.word.flow.ui.home.wordscounter.history.WordsCounterHistoryScreen
import com.word.flow.ui.navigation.LocalAppPadding
import com.word.flow.ui.navigation.LocalHistoryManager
import java.text.DecimalFormat

@Composable
fun WordsCounterScreen(
    vm: WordsCounterViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsState()
    var useMinHeight by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val minHeight = with(density) { 300.dp.toPx() }
    val historyManager = LocalHistoryManager.current
    var showDetails by remember {
        mutableStateOf(false)
    }
    if (historyManager.isHistoryNow) {
        WordsCounterHistoryScreen(onClick = {
            vm.updateTextUsingWordHistoryId(it)
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
            Card(
                Modifier
                    .fillMaxWidth()
                    .run {
                        if (useMinHeight) {
                            height(300.dp)
                        } else weight(1f)
                    }
                    .onPlaced {
                        if (it.size.height < minHeight) {
                            useMinHeight = true
                        }
                    }) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

                    OutlinedTextField(
                        value = state.text,
                        onValueChange = vm::setText,
                        label = { Text("Write text") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    )
                    Button(
                        onClick = vm::proceed, modifier = Modifier.fillMaxWidth()
                    ) { Text("Proceed") }
                }
            }
            Card(Modifier.fillMaxWidth()) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp)
                ) {
                    Text(
                        "Words found: ${state.result}",
                        Modifier
                            .padding(vertical = 12.dp)
                            .padding(start = 6.dp)
                    )
                    TextButton({
                        showDetails = true
                    }, enabled = state.text.isNotBlank()) {
                        Text("Check")
                    }
                }
            }
        }
        if (showDetails) {
            AlertDialog(
                onDismissRequest = {
                    showDetails = false
                },
                title = {
                    Text("Text details")
                },
                text = {
                    var filter by remember {
                        mutableStateOf("")
                    }
                    Column {
                        OutlinedTextField(
                            label = { Text("Filter") },
                            value = filter,
                            onValueChange = { filter = it },
                        )
                        LazyColumn(
                            modifier = Modifier.heightIn(max = 300.dp)
                        ) {
                            val decimalFormat = DecimalFormat("###.##")
                            val wordsToCounts = state.wordsMap.toList().filter {
                                filter in it.first
                            }
                            item {
                                Spacer(Modifier.height(4.dp))
                            }
                            items(wordsToCounts) { (word, count) ->
                                Text(
                                    "$word: $count (${decimalFormat.format((count.toFloat() / state.result) * 100f)}%)",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton({
                        showDetails = false
                    }) {
                        Text("Ok")
                    }
                },
            )
        }
    }
}

@Preview
@Composable
private fun WordsCounterScreenPreview() {
    WordsCounterScreen(
        vm = viewModel {
            val dao = FakeCountersDao()
            WordsCounterViewModel(
                CountWordsUseCase(),
                upsertCountersUseCase = UpsertCountersUseCase(dao),
                getCountersUseCase = GetCountersUseCase(dao),
            )
        },
    )
}