package com.word.flow.ui.home.wordscounter.history

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.word.flow.data.local.db.dao.fake.FakeCountersDao
import com.word.flow.domain.usecase.DeleteCountersUseCase
import com.word.flow.domain.usecase.GetCountersUseCase
import com.word.flow.ui.navigation.LocalAppPadding

@Composable
fun WordsCounterHistoryScreen(
    onClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WordsCounterHistoryViewModel = hiltViewModel(),
) {
    val entities by viewModel.entities.collectAsState()
    var entityIdToDelete by remember {
        mutableStateOf<Long?>(null)
    }
    if (entities.isEmpty()) {
        Box(modifier
            .fillMaxSize()
            .padding(LocalAppPadding.current)
            .padding(16.dp), Alignment.Center) {
            Card {
                Text(
                    "You haven’t counted the number of words in any text.\nYou can go back and count some!",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    } else {
        LazyColumn(
            modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp) + LocalAppPadding.current,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(entities) { item ->
                item(
                    onClick = {
                        onClick(item.id)
                    }, onDelete = {
                        entityIdToDelete = item.id
                    }, modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
    val entityToDelete = entities.firstOrNull { it.id == entityIdToDelete }
    entityToDelete?.let { entityToDelete ->
        AlertDialog(
            onDismissRequest = { entityIdToDelete = null },
            title = { Text("Delete history") },
            text = {
                Column {
                    Text("Are you sure you want to delete this history item? Content of this history item:")
                    Text(entityToDelete.content, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.delete(entityToDelete.id)
                        entityIdToDelete = null
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { entityIdToDelete = null }) {
                    Text("Cancel")
                }
            },
        )
    }
}

@Preview
@Composable
private fun WordsCounterHistoryScreenPreview() {
    val viewModel = viewModel {
        val dao = FakeCountersDao()
        WordsCounterHistoryViewModel(
            getCountersUseCase = GetCountersUseCase(dao),
            deleteCountersUseCase = DeleteCountersUseCase(dao),
        )
    }
    WordsCounterHistoryScreen(onClick = {}, viewModel = viewModel)
}
