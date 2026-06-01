package com.word.flow.ui.home.lettercalculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.word.flow.data.local.db.entity.LetterHistoryEntity
import com.word.flow.domain.usecase.UpsertCountersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LetterCalculatorViewModel @Inject constructor(
    private val upsertCountersUseCase: UpsertCountersUseCase,
) : ViewModel() {
    private var lastText: String = ""
    private val _uiState = MutableStateFlow(LetterCalculatorUiState())
    val uiState: StateFlow<LetterCalculatorUiState> = _uiState.asStateFlow()

    fun setText(value: String) {
        _uiState.value = _uiState.value.copy(text = value)
    }

    fun proceed() {
        showResult()
        if (_uiState.value.text != lastText) {
            lastText = _uiState.value.text
            viewModelScope.launch {
                upsertCountersUseCase(LetterHistoryEntity(content = _uiState.value.text))
            }
        }
    }

    fun proceedHistory() {
        showResult()
    }

    private fun showResult() {
        val value = _uiState.value.text.trim()
        if (isInvalidWord(value)) return

        val frequencies = LinkedHashMap<Char, Int>()
        value.forEach { char ->
            frequencies[char] = (frequencies[char] ?: 0) + 1
        }

        _uiState.value = _uiState.value.copy(
            letterCount = value.length,
            lettersFrequency = frequencies.entries.map { it.key to it.value }
        )
    }

    private fun isInvalidWord(value: String): Boolean {
        if (value.isEmpty()) return true
        if (value.contains(' ')) return true
        if (value.count { it == '-' } > 1) return true
        return false
    }
}
