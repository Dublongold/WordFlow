package com.word.flow.domain.usecase

import com.word.flow.domain.model.WordSelectionItem
import com.word.flow.domain.repository.WordSelectionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSelectableWordsUseCase @Inject constructor(
    private val selectionRepository: WordSelectionRepository,
) {
    suspend operator fun invoke(): List<WordSelectionItem> = selectionRepository.getSelectableWords()
    fun observe(): Flow<List<WordSelectionItem>> = selectionRepository.observeSelectableWords()
}