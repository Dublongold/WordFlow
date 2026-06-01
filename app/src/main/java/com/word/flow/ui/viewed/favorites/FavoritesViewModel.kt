package com.word.flow.ui.viewed.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.word.flow.domain.usecase.GetViewedWordsUseCase
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
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavoritesUiState(
    val isKnown: Boolean = true,
    val query: String = "",
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getViewedWordsUseCase: GetViewedWordsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    val haveAnyWords = getViewedWordsUseCase.favoriteCount().map {
        it > 0
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val words = uiState.flatMapLatest { state ->
        getViewedWordsUseCase.favorites(state.isKnown, state.query)
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
