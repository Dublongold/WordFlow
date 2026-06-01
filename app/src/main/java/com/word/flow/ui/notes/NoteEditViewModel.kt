package com.word.flow.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.word.flow.domain.model.WordNote
import com.word.flow.domain.model.WordSelectionItem
import com.word.flow.domain.model.WordType
import com.word.flow.domain.usecase.DeleteNoteUseCase
import com.word.flow.domain.usecase.GetNoteUseCase
import com.word.flow.domain.usecase.GetSelectableWordsUseCase
import com.word.flow.domain.usecase.SaveWordNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NoteEditUiState(
    val noteId: Long? = null,
    val wordFilterQuery: String = "",
    val selectedWordId: Long = -1,
    val selectedWordType: WordType = WordType.ENCOUNTERED,
    val note: String = "",
    val allWords: List<WordSelectionItem> = emptyList(),
    val done: Boolean = false,
) {
    val filteredWords: List<WordSelectionItem>
        get() {
            if (wordFilterQuery.isBlank()) return allWords
            val q = wordFilterQuery.lowercase()
            return allWords.filter { it.word.lowercase().contains(q) }
        }

    val selectedWord: WordSelectionItem?
        get() = allWords.firstOrNull { it.id == selectedWordId && it.wordType == selectedWordType }
}

@HiltViewModel
class NoteEditViewModel @Inject constructor(
    private val getSelectableWordsUseCase: GetSelectableWordsUseCase,
    private val saveWordNoteUseCase: SaveWordNoteUseCase,
    private val getNoteUseCase: GetNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(NoteEditUiState())
    val uiState: StateFlow<NoteEditUiState> = _uiState.asStateFlow()

    fun load(noteId: Long?, initialWordId: Long, initialWordType: WordType) {
        viewModelScope.launch {
            val selectable = getSelectableWordsUseCase()
            if (noteId == null) {
                val selected = selectable.firstOrNull { it.id == initialWordId && it.wordType == initialWordType }
                    ?: selectable.firstOrNull()
                _uiState.update {
                    val takenWords = getNoteUseCase().map { it.word }
                    it.copy(
                        noteId = null,
                        selectedWordId = selected?.id ?: -1,
                        selectedWordType = selected?.wordType ?: WordType.ENCOUNTERED,
                        allWords = selectable.filter {
                            it.word !in takenWords
                        },
                        note = "",
                        done = false,
                    )
                }
            } else {
                val loaded = getNoteUseCase(noteId)
                _uiState.update {
                    it.copy(
                        noteId = loaded?.id,
                        selectedWordId = loaded?.wordId ?: initialWordId,
                        selectedWordType = loaded?.wordType ?: initialWordType,
                        note = loaded?.note ?: "",
                        allWords = selectable,
                        done = false,
                    )
                }
            }
        }
    }

    fun setWordFilterQuery(value: String) {
        _uiState.update { it.copy(wordFilterQuery = value) }
    }

    fun setSelectedWord(item: WordSelectionItem) {
        _uiState.update { it.copy(selectedWordId = item.id, selectedWordType = item.wordType) }
    }

    fun setNote(value: String) = _uiState.update { it.copy(note = value) }

    fun save() {
        val state = _uiState.value
        if (state.selectedWordId < 0) return
        val selected = state.selectedWord ?: return
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val existingCreatedAt = state.noteId?.let { getNoteUseCase(it)?.createdAt }
            saveWordNoteUseCase(
                WordNote(
                    id = state.noteId ?: 0,
                    wordId = state.selectedWordId,
                    wordType = state.selectedWordType,
                    word = selected.word,
                    note = state.note,
                    createdAt = existingCreatedAt ?: now,
                    updatedAt = now,
                )
            )
            _uiState.update { it.copy(done = true) }
        }
    }

    fun resetDone() {
        _uiState.update { it.copy(done = false) }
    }

    fun delete() {
        val id = _uiState.value.noteId ?: return
        viewModelScope.launch {
            deleteNoteUseCase(id)
            _uiState.update { it.copy(done = true) }
        }
    }
}
