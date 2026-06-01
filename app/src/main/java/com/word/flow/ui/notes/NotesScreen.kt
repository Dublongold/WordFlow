package com.word.flow.ui.notes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import com.word.flow.ui.navigation.AppRoute
import com.word.flow.ui.navigation.LocalAppPadding

@Composable
fun NotesScreen(
    onAdd: () -> Unit,
    onEdit: (Long) -> Unit,
    vm: NotesViewModel = hiltViewModel(),
) {
    val state by vm.uiState.collectAsState()
    val notes by vm.notes.collectAsState()
    val canAdd by vm.canAdd.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(
                space = 12.dp,
                alignment = if (notes.isEmpty()) {
                    Alignment.CenterVertically
                } else Alignment.Top
            ),
        ) {


            if (notes.isEmpty()) {
                if (canAdd) {
                    Card(
                        Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(LocalAppPadding.current)
                    ) {
                        Text(
                            "Add your first note!",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = (52 + 8 + 16).dp, top = 16.dp) + LocalAppPadding.current
                ) {
                    item {
                        Card {
                            OutlinedTextField(
                                value = state.query,
                                onValueChange = vm::setQuery,
                                singleLine = true,
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth(),
                                label = { Text("Filter notes") },
                            )
                        }
                    }
                    items(notes) { note ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onEdit(note.id) },
                        ) {
                            Column(
                                Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(note.word, style = MaterialTheme.typography.titleMedium)
                                Text(note.note)
                            }
                        }
                    }
                }
            }
        }

        if (canAdd) {
            FloatingActionButton(
                onClick = onAdd,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Add note",
                )
            }
        } else {
            Box(
                Modifier
                    .fillMaxSize(0.5f)
                    .padding(16.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                ) {
                    Text(
                        "You have no words to note yet. Go to \"${AppRoute.Home.Swiping.title}\" or \"${AppRoute.Home.Dictionary.TITLE}\" to find new words.",
                        Modifier
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}
