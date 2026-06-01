package com.word.flow.domain.repository

import com.word.flow.domain.model.EncounteredWord
import kotlinx.coroutines.flow.Flow

interface EncounteredWordRepository {
    fun observeKnownCount(): Flow<Int>
    fun observeUnknownCount(): Flow<Int>
    fun observeFavoriteKnownCount(): Flow<Int>
    fun observeFavoriteUnknownCount(): Flow<Int>
    fun observeEncountered(isKnown: Boolean, query: String): Flow<List<EncounteredWord>>
    fun observeFavorites(isKnown: Boolean, query: String): Flow<List<EncounteredWord>>
    suspend fun upsert(word: String, isKnown: Boolean)
    suspend fun setFavorite(word: String, isFavorite: Boolean)
    suspend fun clearAll()
    suspend fun getAllWords(): List<String>
}