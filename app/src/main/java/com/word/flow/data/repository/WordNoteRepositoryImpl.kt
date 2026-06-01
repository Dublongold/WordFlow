package com.word.flow.data.repository

import com.word.flow.data.local.db.dao.WordNoteDao
import com.word.flow.data.mapper.toDomain
import com.word.flow.data.mapper.toEntity
import com.word.flow.domain.model.WordNote
import com.word.flow.domain.repository.WordNoteRepository
import com.word.flow.domain.repository.WordSelectionRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WordNoteRepositoryImpl @Inject constructor(
    private val dao: WordNoteDao,
    private val selectionRepository: WordSelectionRepository,
) : WordNoteRepository {
    override fun observeNotes(query: String, selectedWord: String?) =
        dao.observeNotes(query.toSqlLike(), selectedWord).map { list -> list.map { it.toDomain() } }

    override suspend fun getById(id: Long): WordNote? = dao.getById(id)?.toDomain()

    override suspend fun getAll(): List<WordNote> = dao.getAll().map { it.toDomain() }

    override suspend fun upsert(note: WordNote): Long {
        val word = selectionRepository.getSelectableWords().firstOrNull {
            it.id == note.wordId && it.wordType == note.wordType
        }?.word ?: ""
        return dao.upsert(note.toEntity(word))
    }

    override suspend fun delete(id: Long) = dao.delete(id)

    override suspend fun clearAll() = dao.clearAll()
}