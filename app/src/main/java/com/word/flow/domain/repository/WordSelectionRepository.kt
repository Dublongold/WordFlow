package com.word.flow.domain.repository

import com.word.flow.domain.model.WordSelectionItem
import kotlinx.coroutines.flow.Flow

interface WordSelectionRepository {
    suspend fun getSelectableWords(): List<WordSelectionItem>
    fun observeSelectableWords(): Flow<List<WordSelectionItem>>
}
