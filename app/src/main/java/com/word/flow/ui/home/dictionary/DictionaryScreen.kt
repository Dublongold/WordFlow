package com.word.flow.ui.home.dictionary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.word.flow.ui.navigation.LocalAppPadding

@Composable
fun DictionaryScreen(
    presetWord: String?,
    vm: DictionaryViewModel = hiltViewModel(),
) {
    val state by vm.uiState.collectAsState()

    LaunchedEffect(presetWord) {
        if (!presetWord.isNullOrBlank()) {
            vm.setQuery(presetWord)
            vm.search(presetWord)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(LocalAppPadding.current)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {

        Card {
            OutlinedTextField(
                value = state.query,
                onValueChange = vm::setQuery,
                label = { Text("Enter content") },
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
            )
        }
        Button(
            onClick = { vm.search() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        ) {
            Text("Search")
        }
        if (state.loading) CircularProgressIndicator()
        when (state.error) {
            DictionaryError.NoWordFound -> {
                Text(
                    "The content \"${state.query}\" can't be found!",
                    color = MaterialTheme.colorScheme.error
                )
            }

            DictionaryError.NoNetwork -> {
                Text("No network connection!", color = MaterialTheme.colorScheme.error)
            }

            else -> {}
        }
        state.word?.let { word ->
            Card {
                Column(
                    Modifier.fillMaxWidth().padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(word.word, style = MaterialTheme.typography.headlineMedium)
                    word.phonetic?.let { Text("Phonetic: $it") }
                    word.origin?.let { Text("Origin: $it") }
                    Text("Meanings", style = MaterialTheme.typography.titleMedium)
                    word.meanings.forEach { meaning ->
                        Text("${meaning.partOfSpeech}: ${meaning.definition}")
                    }
                }
            }
        }
    }
}
