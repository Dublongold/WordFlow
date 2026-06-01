package com.word.flow.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.word.flow.domain.repository.fake.FakeCatalogedWordRepository
import com.word.flow.domain.repository.fake.FakeEncounteredWordRepository
import com.word.flow.domain.repository.fake.FakePreferencesRepository
import com.word.flow.domain.repository.fake.FakeWordNoteRepository
import com.word.flow.domain.usecase.ClearCatalogedWordsUseCase
import com.word.flow.domain.usecase.ClearNotesUseCase
import com.word.flow.domain.usecase.ClearViewedWordsUseCase
import com.word.flow.domain.usecase.ObserveFilterQueryUseCase
import com.word.flow.ui.navigation.AppRoute
import com.word.flow.ui.navigation.LocalAppPadding

@Composable
fun SettingsScreen(
    vm: SettingsViewModel = hiltViewModel(),
) {
    val pendingActionState = remember { mutableStateOf<SettingsAction?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(LocalAppPadding.current)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SettingsDeleteButton(
            label = "Clear viewed words",
            description = "Clears all viewed words from \"${AppRoute.Home.Dictionary.TITLE}\" or \"${AppRoute.Home.Swiping.title}\"",
            onClick = { pendingActionState.value = SettingsAction.ClearViewed },
            modifier = Modifier.fillMaxWidth()
        )

        SettingsDeleteButton(
            label = "Clear dictionary cache",
            description = "Clears all words, that was found in \"${AppRoute.Home.Dictionary.TITLE}\"",
            onClick = { pendingActionState.value = SettingsAction.ClearCataloged },
            modifier = Modifier.fillMaxWidth()
        )

        SettingsDeleteButton(
            label = "Clear all notes",
            description = "Clears every notes, that was created in \"${AppRoute.Notes.title}\"",
            onClick = { pendingActionState.value = SettingsAction.ClearNotes },
            modifier = Modifier.fillMaxWidth()
        )
        SettingsDeleteButton(
            label = "Clear all data",
            description = "Clears all data of your app, including dictionary cache, viewed words and notes!",
            onClick = { pendingActionState.value = SettingsAction.ClearAll },
            modifier = Modifier.fillMaxWidth()
        )
    }

    pendingActionState.value?.let { action ->
        ConfirmSettingsActionDialog(
            action = action,
            onDismiss = { pendingActionState.value = null },
            onConfirm = {
                when (action) {
                    SettingsAction.ClearViewed -> vm.clearViewed()
                    SettingsAction.ClearCataloged -> vm.clearCataloged()
                    SettingsAction.ClearNotes -> vm.clearNotes()
                    SettingsAction.ClearAll -> vm.clearAll()
                }
                pendingActionState.value = null
            }
        )
    }
}

private enum class SettingsAction(
    val title: String,
    val message: String,
    val confirmLabel: String,
) {
    ClearViewed(
        title = "Clear viewed words?",
        message = "This will permanently remove all viewed words.",
        confirmLabel = "Clear"
    ),
    ClearCataloged(
        title = "Clear dictionary cache?",
        message = "This will permanently remove all cached dictionary entries.",
        confirmLabel = "Clear"
    ),
    ClearNotes(
        title = "Clear all notes?",
        message = "This will permanently remove all your notes.",
        confirmLabel = "Clear"
    ),
    ClearAll(
        title = "Clear all app data?",
        message = "This will permanently remove viewed words, dictionary cache, and notes.",
        confirmLabel = "Clear all"
    ),
}

@Composable
private fun ConfirmSettingsActionDialog(
    action: SettingsAction,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(action.title) },
        text = { Text(action.message) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(action.confirmLabel)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    Surface {
        SettingsScreen(
            vm = viewModel {
                val preferencesRepository = FakePreferencesRepository()
                val catalogedWordRepository = FakeCatalogedWordRepository()
                val encounteredWordRepository = FakeEncounteredWordRepository()
                val wordNoteRepository = FakeWordNoteRepository()
                SettingsViewModel(
                    observeFilterQueryUseCase = ObserveFilterQueryUseCase(
                        preferencesRepository = preferencesRepository
                    ),
                    clearViewedWordsUseCase = ClearViewedWordsUseCase(
                        encounteredWordRepository = encounteredWordRepository
                    ),
                    clearCatalogedWordsUseCase = ClearCatalogedWordsUseCase(
                        catalogedWordRepository = catalogedWordRepository
                    ),
                    clearNotesUseCase = ClearNotesUseCase(
                        wordNoteRepository = wordNoteRepository
                    )
                )
            }
        )
    }
}