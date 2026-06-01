package com.word.flow.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.word.flow.domain.usecase.ClearCatalogedWordsUseCase
import com.word.flow.domain.usecase.ClearNotesUseCase
import com.word.flow.domain.usecase.ClearViewedWordsUseCase
import com.word.flow.domain.usecase.ObserveFilterQueryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class SettingsUiState(
    val filterQuery: String = "",
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    observeFilterQueryUseCase: ObserveFilterQueryUseCase,
    private val clearViewedWordsUseCase: ClearViewedWordsUseCase,
    private val clearCatalogedWordsUseCase: ClearCatalogedWordsUseCase,
    private val clearNotesUseCase: ClearNotesUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        observeFilterQueryUseCase().onEach { query ->
            _uiState.value = _uiState.value.copy(filterQuery = query)
        }.launchIn(viewModelScope)
    }

    fun clearViewed() {
        viewModelScope.launch { clearViewedWordsUseCase() }
    }

    fun clearCataloged() {
        viewModelScope.launch { clearCatalogedWordsUseCase() }
    }

    fun clearNotes() {
        viewModelScope.launch { clearNotesUseCase() }
    }
    fun clearAll() {
        viewModelScope.launch {
            clearViewedWordsUseCase()
            clearCatalogedWordsUseCase()
            clearNotesUseCase()
        }
    }
}
