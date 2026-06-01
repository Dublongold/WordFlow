package com.word.flow.data.local.db.dao.fake

import com.word.flow.data.local.db.dao.SeedWordDao
import com.word.flow.data.local.db.entity.SeedWordEntity
import kotlinx.coroutines.flow.MutableStateFlow

@Suppress("unused")
class FakeSeedWordDao : SeedWordDao {
    private val seedWordsState = MutableStateFlow<List<SeedWordEntity>>(emptyList())
    private val encounteredWordsState = MutableStateFlow<Set<String>>(emptySet())

    override suspend fun insertAll(words: List<SeedWordEntity>) {
        var current = seedWordsState.value
        words.forEach { word ->
            val exists = current.any { it.word == word.word }
            if (!exists) {
                val id = if (word.id > 0L) word.id else (current.maxOfOrNull { it.id } ?: 0L) + 1L
                current = current + word.copy(id = id)
            }
        }
        seedWordsState.value = current
    }

    override suspend fun count(): Int = seedWordsState.value.size

    override suspend fun getSwipePool(): List<String> {
        val encountered = encounteredWordsState.value
        return seedWordsState.value
            .sortedBy { it.id }
            .map { it.word }
            .filterNot { it in encountered }
    }

    fun setEncounteredWords(words: Set<String>) {
        encounteredWordsState.value = words
    }
}
