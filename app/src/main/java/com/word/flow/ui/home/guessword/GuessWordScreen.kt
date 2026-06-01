package com.word.flow.ui.home.guessword

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.word.flow.R
import com.word.flow.ui.navigation.LocalAppPadding

@Composable
fun GuessWordScreen(vm: GuessWordViewModel = hiltViewModel()) {
    val state by vm.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(LocalAppPadding.current)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            FilledIconButton(
                onClick = vm::restartGame,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_restart),
                    contentDescription = "Restart game",
                )
            }
        }

        Card(Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    if (state.isGuessed) state.currentWord.orEmpty() else state.maskedWord,
                )
                Text("Tries: ${state.tries}")
                if (state.isGuessed) {
                    Text("You guessed the content in ${state.tries} tries.")
                }
                state.hint?.let { Text(it) }
            }
        }

        if (!state.isGuessed) {
            Card(Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    OutlinedTextField(
                        value = state.guess,
                        onValueChange = vm::onGuessChange,
                        label = { Text("Type 1 letter") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = state.currentWord != null,
                    )
                    Button(
                        onClick = vm::submitGuess,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = state.currentWord != null && state.guess.isNotEmpty(),
                    ) {
                        Text("Check letter")
                    }
                }
            }
        }

        if (state.isGuessed) {
            Button(
                onClick = vm::restartGame,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Try again")
            }
        }
    }
}
