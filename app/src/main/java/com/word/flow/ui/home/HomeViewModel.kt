package com.word.flow.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.word.flow.domain.model.WordDetail
import com.word.flow.domain.usecase.ObserveHomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

data class HomeUiState(
    val knownCount: Int = 0,
    val unknownCount: Int = 0,
    val randomWord: WordDetail? = null,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    observeHomeUseCase: ObserveHomeUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observeHomeUseCase().onEach { data ->
            _uiState.value = HomeUiState(
                knownCount = data.knownCount,
                unknownCount = data.unknownCount,
                randomWord = data.randomWord,
            )
        }.launchIn(viewModelScope)
    }
}
