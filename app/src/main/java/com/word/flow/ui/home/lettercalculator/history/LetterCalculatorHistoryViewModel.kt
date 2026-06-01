package com.word.flow.ui.home.lettercalculator.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.word.flow.domain.usecase.DeleteCountersUseCase
import com.word.flow.domain.usecase.GetCountersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private fun countLetters(content: String): Int = content.trim().length

@HiltViewModel
class LetterCalculatorHistoryViewModel @Inject constructor(
    getCountersUseCase: GetCountersUseCase,
    private val deleteCountersUseCase: DeleteCountersUseCase,
) : ViewModel() {
    val entities: StateFlow<List<LetterCalculatorHistoryItem>> =
        getCountersUseCase.observeLetterHistory().map { entities ->
            entities.map {
                LetterCalculatorHistoryItem(
                    it.id,
                    it.content,
                    countLetters(it.content),
                )
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun delete(id: Long) {
        viewModelScope.launch {
            deleteCountersUseCase.deleteLetterHistory(id)
        }
    }
}
