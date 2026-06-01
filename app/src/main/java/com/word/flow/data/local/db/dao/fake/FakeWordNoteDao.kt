package com.word.flow.data.local.db.dao.fake

import com.word.flow.data.local.db.dao.WordNoteDao
import com.word.flow.data.local.db.entity.WordNoteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

@Suppress("unused")
class FakeWordNoteDao : WordNoteDao {
    private val notesState = MutableStateFlow<List<WordNoteEntity>>(emptyList())

    override fun observeNotes(query: String, selectedWord: String?): Flow<List<WordNoteEntity>> {
        return notesState.map { notes ->
            notes
                .filter { selectedWord == null || it.word == selectedWord }
                .filter {
                    query.isBlank() ||
                        matchesLike(it.word, query) ||
                        matchesLike(it.note, query)
                }
                .sortedByDescending { it.updatedAt }
        }
    }

    override suspend fun getAll(): List<WordNoteEntity> = notesState.value

    override suspend fun getById(id: Long): WordNoteEntity? = notesState.value.firstOrNull { it.id == id }

    override suspend fun upsert(entity: WordNoteEntity): Long {
        val existing = notesState.value.firstOrNull { it.word == entity.word }
        val id = when {
            entity.id > 0L -> entity.id
            existing != null -> existing.id
            else -> nextId()
        }

        val updated = entity.copy(id = id)
        notesState.value = notesState.value
            .filterNot { it.id == id || it.word == entity.word }
            .plus(updated)
        return id
    }

    override suspend fun delete(id: Long) {
        notesState.value = notesState.value.filterNot { it.id == id }
    }

    override suspend fun clearAll() {
        notesState.value = emptyList()
    }

    private fun nextId(): Long = (notesState.value.maxOfOrNull { it.id } ?: 0L) + 1L

    private fun matchesLike(value: String, likePattern: String): Boolean {
        if (likePattern.isBlank()) return true
        val escapedPattern = Regex.escape(likePattern).replace("%", ".*")
        return Regex("^$escapedPattern$", setOf(RegexOption.IGNORE_CASE)).matches(value)
    }
}
