package com.word.flow.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.word.flow.domain.usecase.GetSelectableWordsUseCase
import com.word.flow.domain.usecase.ObserveNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class NotesUiState(
    val query: String = "",
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class NotesViewModel @Inject constructor(
    private val observeNotesUseCase: ObserveNotesUseCase,
    getSelectableWordsUseCase: GetSelectableWordsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(NotesUiState())
    val uiState: StateFlow<NotesUiState> = _uiState.asStateFlow()

    val canAdd = getSelectableWordsUseCase.observe().map { it.isNotEmpty() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    val notes = uiState.flatMapLatest { state ->
        observeNotesUseCase(state.query, null)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun setQuery(value: String) {
        _uiState.update { it.copy(query = value) }
    }
}
