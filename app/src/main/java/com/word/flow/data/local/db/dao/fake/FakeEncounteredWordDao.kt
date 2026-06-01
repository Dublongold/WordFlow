package com.word.flow.data.local.db.dao.fake

import com.word.flow.data.local.db.dao.EncounteredWordDao
import com.word.flow.data.local.db.entity.EncounteredWordEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeEncounteredWordDao : EncounteredWordDao {
    private val encounteredState = MutableStateFlow<List<EncounteredWordEntity>>(emptyList())

    override fun observeKnownCount(): Flow<Int> = encounteredState.map { list -> list.count { it.isKnown } }

    override fun observeUnknownCount(): Flow<Int> = encounteredState.map { list -> list.count { !it.isKnown } }

    override fun observeFavoriteKnownCount(): Flow<Int> = encounteredState.map { list -> list.count { it.isKnown && it.isFavorite } }

    override fun observeFavoriteUnknownCount(): Flow<Int> = encounteredState.map { list -> list.count { !it.isKnown && it.isFavorite } }

    override fun observeEncountered(isKnown: Boolean, query: String): Flow<List<EncounteredWordEntity>> {
        return encounteredState.map { list ->
            list
                .filter { it.isKnown == isKnown }
                .filter { query.isBlank() || matchesLike(it.word, query) }
                .sortedByDescending { it.swipedAt }
        }
    }

    override fun observeFavorites(isKnown: Boolean, query: String): Flow<List<EncounteredWordEntity>> {
        return encounteredState.map { list ->
            list
                .filter { it.isKnown == isKnown }
                .filter { it.isFavorite }
                .filter { query.isBlank() || matchesLike(it.word, query) }
                .sortedByDescending { it.swipedAt }
        }
    }

    override suspend fun getByWord(word: String): EncounteredWordEntity? =
        encounteredState.value.firstOrNull { it.word == word }

    override suspend fun insert(entity: EncounteredWordEntity): Long {
        val id = when {
            entity.id > 0L -> entity.id
            else -> encounteredState.value.firstOrNull { it.word == entity.word }?.id ?: nextId()
        }

        val updated = entity.copy(id = id)
        encounteredState.value = encounteredState.value
            .filterNot { it.id == id || it.word == entity.word }
            .plus(updated)
        return id
    }

    override suspend fun setFavorite(word: String, isFavorite: Boolean) {
        encounteredState.value = encounteredState.value.map {
            if (it.word.equals(word, ignoreCase = true)) it.copy(isFavorite = isFavorite) else it
        }
    }

    override suspend fun clearAll() {
        encounteredState.value = emptyList()
    }

    override fun observeAllWords(): Flow<List<String>> = encounteredState.map { list -> list.map { it.word } }

    override suspend fun getAllWords(): List<String> = encounteredState.value.map { it.word }

    private fun nextId(): Long = (encounteredState.value.maxOfOrNull { it.id } ?: 0L) + 1L

    private fun matchesLike(value: String, likePattern: String): Boolean {
        if (likePattern.isBlank()) return true
        val escapedPattern = Regex.escape(likePattern).replace("%", ".*")
        return Regex("^$escapedPattern$", setOf(RegexOption.IGNORE_CASE)).matches(value)
    }
}
