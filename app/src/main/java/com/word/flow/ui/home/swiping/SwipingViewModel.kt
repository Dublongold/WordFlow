package com.word.flow.ui.home.swiping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.word.flow.domain.usecase.GetSwipePoolUseCase
import com.word.flow.domain.usecase.SwipeWordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SwipingUiState(
    val words: List<String> = emptyList(),
    val currentIndex: Int = 0,
) {
    val currentWord: String? get() = words.getOrNull(currentIndex)
    val nextWord: String? get() = words.getOrNull(currentIndex + 1)
    val isFinished: Boolean get() = words.isEmpty() || currentIndex >= words.size
}

@HiltViewModel
class SwipingViewModel @Inject constructor(
    private val getSwipePoolUseCase: GetSwipePoolUseCase,
    private val swipeWordUseCase: SwipeWordUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SwipingUiState())
    val uiState: StateFlow<SwipingUiState> = _uiState.asStateFlow()

    init {
        reload()
    }

    fun reload() {
        viewModelScope.launch {
            val pool = getSwipePoolUseCase()
            _uiState.value = SwipingUiState(words = pool)
        }
    }

    fun swipeKnown() = swipe(true)
    fun swipeUnknown() = swipe(false)

    private fun swipe(isKnown: Boolean) {
        val word = _uiState.value.currentWord ?: return
        viewModelScope.launch {
            swipeWordUseCase(word, isKnown)
            _uiState.value = _uiState.value.copy(currentIndex = _uiState.value.currentIndex + 1)
        }
    }
}
