package com.word.flow.domain.repository

import com.word.flow.domain.model.CatalogedWord
import kotlinx.coroutines.flow.Flow

interface CatalogedWordRepository {
    suspend fun getByWord(word: String): CatalogedWord?
    suspend fun fetchAndCache(word: String): Result<CatalogedWord>
    fun observeRandom(): Flow<CatalogedWord?>
    suspend fun clearAll()
    suspend fun getAllWords(): List<String>
}