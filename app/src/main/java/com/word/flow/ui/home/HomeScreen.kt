package com.word.flow.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.word.flow.R
import com.word.flow.ui.navigation.LocalAppPadding
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onDictionary: (String?) -> Unit,
    onSwiping: () -> Unit,
    onCounter: () -> Unit,
    onLetterCalculator: () -> Unit,
    onGuessWord: () -> Unit,
    snackbarHostState: SnackbarHostState?,
    vm: HomeViewModel = hiltViewModel(),
) {
    val state by vm.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp) + LocalAppPadding.current,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Row(
                modifier = Modifier.height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                val values = listOf(
                    "Known words:" to state.knownCount,
                    "Unknown words:" to state.unknownCount,
                )
                for (value in values) {
                    Card(
                        Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                value.first,
                                style = MaterialTheme.typography.titleLarge,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "${value.second}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.bodyLarge.fontSize * 2,
                            )
                        }
                    }
                }
            }
        }
        item {
            val scope = rememberCoroutineScope()
            Card(
                onClick = {
                    state.randomWord?.word?.also {
                        onDictionary(it)
                    } ?: scope.launch {
                        snackbarHostState?.showSnackbar("No random word available yet. Go to \"Dictionary\" to add some words.")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Random word", style = MaterialTheme.typography.titleMedium)
                    Text(state.randomWord?.word ?: "No cataloged words yet")
                    state.randomWord?.phonetic?.let { Text("Phonetic: $it") }
                    val firstMeaning = state.randomWord?.meanings?.firstOrNull()
                    if (firstMeaning != null) {
                        Text("${firstMeaning.partOfSpeech}: ${firstMeaning.definition}")
                    }
                }
            }
        }
        item { MenuCard("Dictionary") { onDictionary(null) } }
        item { MenuCard("Word Swiping") { onSwiping() } }
        item { MenuCard("Words Counter") { onCounter() } }
        item { MenuCard("Letter Calculator") { onLetterCalculator() } }
        item { MenuCard("Guess Word") { onGuessWord() } }
    }
}

@Composable
private fun MenuCard(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onClick,
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, Modifier.padding(16.dp))
            Icon(
                painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = "Go",
                Modifier.padding(16.dp)
            )
        }
    }
}
