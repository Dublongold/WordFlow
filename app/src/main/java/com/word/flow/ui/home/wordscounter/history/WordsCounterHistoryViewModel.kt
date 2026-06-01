package com.word.flow.ui.home.wordscounter.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.word.flow.data.local.db.model.ShortWordHistoryEntity
import com.word.flow.domain.usecase.DeleteCountersUseCase
import com.word.flow.domain.usecase.GetCountersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordsCounterHistoryViewModel @Inject constructor(
    getCountersUseCase: GetCountersUseCase,
    private val deleteCountersUseCase: DeleteCountersUseCase,
) : ViewModel() {
    val entities: StateFlow<List<WordsCounterHistoryItem>> =
        getCountersUseCase.observeTruncatedWordHistory().map { entities ->
            entities.map(ShortWordHistoryEntity::toWordsCounterHistoryItem)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun delete(id: Long) {
        viewModelScope.launch {
            deleteCountersUseCase.deleteWordHistory(id)
        }
    }
}