package com.word.flow.domain.repository.fake

import com.word.flow.domain.model.CatalogedWord
import com.word.flow.domain.repository.CatalogedWordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.util.Locale
import kotlin.random.Random

class FakeCatalogedWordRepository(
    initialWords: List<CatalogedWord> = emptyList(),
) : CatalogedWordRepository {

    private val words = MutableStateFlow(initialWords)

    override suspend fun getByWord(word: String): CatalogedWord? {
        val normalized = word.trim().lowercase(Locale.getDefault())
        return words.value.firstOrNull { it.word.lowercase(Locale.getDefault()) == normalized }
    }

    override suspend fun fetchAndCache(word: String): Result<CatalogedWord> {
        val normalized = word.trim().lowercase(Locale.getDefault())
        if (normalized.isBlank()) {
            return Result.failure(IllegalArgumentException("Word must not be blank"))
        }

        val existing = getByWord(normalized)
        if (existing != null) return Result.success(existing)

        val created = CatalogedWord(
            id = nextId(),
            word = normalized,
            phonetic = null,
            origin = null,
            meanings = emptyList(),
            loadedAt = System.currentTimeMillis(),
        )
        words.value += created
        return Result.success(created)
    }

    override fun observeRandom(): Flow<CatalogedWord?> = words.map { list ->
        if (list.isEmpty()) null else list[Random.nextInt(list.size)]
    }

    override suspend fun clearAll() {
        words.value = emptyList()
    }

    override suspend fun getAllWords(): List<String> = words.value.map { it.word }

    private fun nextId(): Long = (words.value.maxOfOrNull { it.id } ?: 0L) + 1L
}
