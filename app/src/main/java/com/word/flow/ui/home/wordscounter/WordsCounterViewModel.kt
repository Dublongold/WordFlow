package com.word.flow.ui.home.wordscounter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.word.flow.data.local.db.entity.WordHistoryEntity
import com.word.flow.domain.usecase.CountWordsUseCase
import com.word.flow.domain.usecase.GetCountersUseCase
import com.word.flow.domain.usecase.UpsertCountersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordsCounterViewModel @Inject constructor(
    private val countWordsUseCase: CountWordsUseCase,
    private val getCountersUseCase: GetCountersUseCase,
    private val upsertCountersUseCase: UpsertCountersUseCase
) : ViewModel() {
    private var lastText: String = ""
    private val _uiState = MutableStateFlow(WordsCounterUiState())
    val uiState: StateFlow<WordsCounterUiState> = _uiState.asStateFlow()

    fun setText(value: String) {
        _uiState.update { it.copy(text = value) }
    }
    
    fun updateTextUsingWordHistoryId(wordHistoryId: Long) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    text = getCountersUseCase.getWordHistoryById(wordHistoryId)?.content ?: "",
                )
            }
        }
    }
    
    

    private fun showResult() {
        _uiState.value = _uiState.value.copy(result = countWordsUseCase(_uiState.value.text))
    }

    fun proceed() {
        showResult()
        if (_uiState.value.text != lastText) {
            lastText = _uiState.value.text
            _uiState.update {
                it.copy(
                    wordsMap = countWordsUseCase.wordCountMap(lastText)
                )
            }
            viewModelScope.launch {
                upsertCountersUseCase(WordHistoryEntity(
                    content = _uiState.value.text,
                    count = _uiState.value.result
                ))
            }
        }
    }

    fun proceedHistory() {
        showResult()
    }
}
