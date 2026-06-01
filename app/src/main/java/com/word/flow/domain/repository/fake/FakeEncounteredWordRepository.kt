package com.word.flow.domain.repository.fake

import com.word.flow.domain.model.EncounteredWord
import com.word.flow.domain.repository.EncounteredWordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.util.Locale

class FakeEncounteredWordRepository(
    initialWords: List<EncounteredWord> = emptyList(),
) : EncounteredWordRepository {

    private val encountered = MutableStateFlow(initialWords)

    override fun observeKnownCount(): Flow<Int> = encountered.map { list -> list.count { it.isKnown } }

    override fun observeUnknownCount(): Flow<Int> = encountered.map { list -> list.count { !it.isKnown } }

    override fun observeFavoriteKnownCount(): Flow<Int> = encountered.map { list -> list.count { it.isKnown && it.isFavorite } }

    override fun observeFavoriteUnknownCount(): Flow<Int> = encountered.map { list -> list.count { !it.isKnown && it.isFavorite } }

    override fun observeEncountered(isKnown: Boolean, query: String): Flow<List<EncounteredWord>> {
        val normalized = query.trim().lowercase(Locale.getDefault())
        return encountered.map { list ->
            list
                .filter { it.isKnown == isKnown }
                .filter {
                    normalized.isBlank() || it.word.lowercase(Locale.getDefault()).contains(normalized)
                }
                .sortedByDescending { it.swipedAt }
        }
    }

    override fun observeFavorites(isKnown: Boolean, query: String): Flow<List<EncounteredWord>> {
        val normalized = query.trim().lowercase(Locale.getDefault())
        return encountered.map { list ->
            list
                .filter { it.isKnown == isKnown }
                .filter { it.isFavorite }
                .filter {
                    normalized.isBlank() || it.word.lowercase(Locale.getDefault()).contains(normalized)
                }
                .sortedByDescending { it.swipedAt }
        }
    }

    override suspend fun upsert(word: String, isKnown: Boolean) {
        val normalized = word.trim()
        if (normalized.isBlank()) return

        val existing = encountered.value.firstOrNull {
            it.word.equals(normalized, ignoreCase = true)
        }

        val now = System.currentTimeMillis()
        val updated = EncounteredWord(
            id = existing?.id ?: nextId(),
            word = normalized,
            isKnown = isKnown,
            isFavorite = existing?.isFavorite ?: false,
            swipedAt = now,
        )

        encountered.value = encountered.value
            .filterNot { it.id == updated.id }
            .plus(updated)
    }

    override suspend fun setFavorite(word: String, isFavorite: Boolean) {
        encountered.value = encountered.value.map {
            if (it.word.equals(word, ignoreCase = true)) it.copy(isFavorite = isFavorite) else it
        }
    }

    override suspend fun clearAll() {
        encountered.value = emptyList()
    }

    override suspend fun getAllWords(): List<String> = encountered.value.map { it.word }

    private fun nextId(): Long = (encountered.value.maxOfOrNull { it.id } ?: 0L) + 1L
}
