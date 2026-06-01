package com.word.flow.domain.repository

import com.word.flow.domain.model.WordNote
import kotlinx.coroutines.flow.Flow

interface WordNoteRepository {
    fun observeNotes(query: String, selectedWord: String?): Flow<List<WordNote>>
    suspend fun getById(id: Long): WordNote?
    suspend fun getAll(): List<WordNote>
    suspend fun upsert(note: WordNote): Long
    suspend fun delete(id: Long)
    suspend fun clearAll()
}