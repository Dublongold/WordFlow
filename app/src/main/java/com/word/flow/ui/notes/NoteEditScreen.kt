package com.word.flow.ui.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.word.flow.R
import com.word.flow.domain.model.WordType
import com.word.flow.ui.navigation.LocalAppPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditScreen(
    noteId: Long?,
    wordId: Long,
    wordType: WordType,
    onDone: () -> Unit,
    vm: NoteEditViewModel = hiltViewModel(),
) {
    val state by vm.uiState.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(noteId, wordId, wordType) {
        vm.load(noteId = noteId, initialWordId = wordId, initialWordType = wordType)
    }

    LaunchedEffect(state.done) {
        if (state.done) {
            vm.resetDone()
            onDone()
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete note") },
            text = { Text("Are you sure you want to delete this note?") },
            confirmButton = {
                Button(onClick = {
                    showDeleteDialog = false
                    vm.delete()
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            },
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                    value = state.wordFilterQuery,
                    onValueChange = vm::setWordFilterQuery,
                    label = { Text("Filter words") },
                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                )
            }
            val canSelectWord = state.filteredWords.isNotEmpty()
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded && canSelectWord },
            ) {
                val moreThan10 = state.filteredWords.size > 10
                val words = state.filteredWords.take(10)
                val currentWord = if (canSelectWord) {
                    state.selectedWord?.word ?: ""
                } else "No words found"
                Card {
                    OutlinedTextField(
                        value = currentWord,
                        onValueChange = {},
                        readOnly = true,
                        enabled = canSelectWord,
                        label = { Text("Select content" + if (moreThan10) " (top 10)" else "") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .padding(12.dp)
                            .menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth(),
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(0.92f),
                ) {
                    words.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item.word) },
                            onClick = {
                                vm.setSelectedWord(item)
                                expanded = false
                            },
                        )
                    }
                }
            }


            Card {
                OutlinedTextField(
                    value = state.note,
                    onValueChange = vm::setNote,
                    label = { Text("Note") },
                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                    minLines = 6,
                )
            }

            if (noteId != null) {
                Button(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Delete note")
                }
            }
            Spacer(Modifier.height(52.dp))
        }

        FloatingActionButton(
            onClick = vm::save,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(LocalAppPadding.current)
                .padding(16.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_save),
                contentDescription = "Save note",
            )
        }
    }
}
