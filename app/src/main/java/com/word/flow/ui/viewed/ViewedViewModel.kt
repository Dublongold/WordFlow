package com.word.flow.ui.viewed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.word.flow.domain.usecase.GetViewedWordsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class ViewedUiState(
    val isKnown: Boolean = true,
    val query: String = "",
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ViewedViewModel @Inject constructor(
    private val getViewedWordsUseCase: GetViewedWordsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ViewedUiState())
    val uiState: StateFlow<ViewedUiState> = _uiState.asStateFlow()

    val haveAnyWords = getViewedWordsUseCase.count().map {
        it > 0
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val words = uiState.flatMapLatest { state ->
        getViewedWordsUseCase(state.isKnown, state.query)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setKnown(value: Boolean) {
        _uiState.update { it.copy(isKnown = value) }
    }

    fun setQuery(value: String) {
        _uiState.update { it.copy(query = value) }
    }

    fun setFavorite(word: String, isFavorite: Boolean) {
        viewModelScope.launch {
            getViewedWordsUseCase.setFavorite(word = word, isFavorite = isFavorite)
        }
    }
}
