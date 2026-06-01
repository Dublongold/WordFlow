package com.word.flow.domain.repository.fake

import com.word.flow.domain.model.WordNote
import com.word.flow.domain.repository.WordNoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.util.Locale

class FakeWordNoteRepository(
    initialNotes: List<WordNote> = emptyList(),
) : WordNoteRepository {

    private val notes = MutableStateFlow(initialNotes)

    override fun observeNotes(query: String, selectedWord: String?): Flow<List<WordNote>> {
        val normalizedQuery = query.trim().lowercase(Locale.getDefault())
        val normalizedWord = selectedWord?.trim()?.lowercase(Locale.getDefault())

        return notes.map { list ->
            list
                .filter {
                    normalizedQuery.isBlank() ||
                        it.note.lowercase(Locale.getDefault()).contains(normalizedQuery) ||
                        it.word.lowercase(Locale.getDefault()).contains(normalizedQuery)
                }
                .filter {
                    normalizedWord.isNullOrBlank() ||
                        it.word.lowercase(Locale.getDefault()) == normalizedWord
                }
                .sortedByDescending { it.updatedAt }
        }
    }

    override suspend fun getById(id: Long): WordNote? = notes.value.firstOrNull { it.id == id }

    override suspend fun getAll(): List<WordNote> = notes.value

    override suspend fun upsert(note: WordNote): Long {
        val id = if (note.id > 0) note.id else nextId()
        val now = System.currentTimeMillis()
        val existing = notes.value.firstOrNull { it.id == id }

        val updated = note.copy(
            id = id,
            createdAt = existing?.createdAt ?: note.createdAt.takeIf { it > 0 } ?: now,
            updatedAt = now,
        )

        notes.value = notes.value
            .filterNot { it.id == id }
            .plus(updated)

        return id
    }

    override suspend fun delete(id: Long) {
        notes.value = notes.value.filterNot { it.id == id }
    }

    override suspend fun clearAll() {
        notes.value = emptyList()
    }

    private fun nextId(): Long = (notes.value.maxOfOrNull { it.id } ?: 0L) + 1L
}
