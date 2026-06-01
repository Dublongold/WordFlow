package com.word.flow.data.local.db.dao.fake

import com.word.flow.data.local.db.dao.CatalogedWordDao
import com.word.flow.data.local.db.entity.CatalogedMeaningEntity
import com.word.flow.data.local.db.entity.CatalogedWordEntity
import com.word.flow.data.local.db.entity.CatalogedWordWithMeanings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlin.random.Random

@Suppress("unused")
class FakeCatalogedWordDao : CatalogedWordDao {
    private val wordsState = MutableStateFlow<List<CatalogedWordEntity>>(emptyList())
    private val meaningsState = MutableStateFlow<List<CatalogedMeaningEntity>>(emptyList())

    override suspend fun getByWord(word: String): CatalogedWordWithMeanings? {
        val entity = wordsState.value.firstOrNull { it.word == word } ?: return null
        return relationFor(entity)
    }

    override fun observeRandomId(): Flow<Long?> = wordsState.map { words ->
        if (words.isEmpty()) null else words[Random.nextInt(words.size)].id
    }

    override suspend fun getById(id: Long): CatalogedWordWithMeanings? {
        val entity = wordsState.value.firstOrNull { it.id == id } ?: return null
        return relationFor(entity)
    }

    override suspend fun insertWord(entity: CatalogedWordEntity): Long {
        val existing = wordsState.value.firstOrNull { it.word == entity.word }
        val id = when {
            entity.id > 0L -> entity.id
            existing != null -> existing.id
            else -> nextWordId()
        }

        val updated = entity.copy(id = id)
        wordsState.value = wordsState.value.filterNot { it.id == id || it.word == entity.word } + updated
        return id
    }

    override suspend fun insertMeanings(entities: List<CatalogedMeaningEntity>) {
        var current = meaningsState.value
        entities.forEach { entity ->
            val id = if (entity.id > 0L) entity.id else nextMeaningId(current)
            val updated = entity.copy(id = id)
            current = current.filterNot { it.id == id } + updated
        }
        meaningsState.value = current
    }

    override suspend fun clearMeanings(wordId: Long) {
        meaningsState.value = meaningsState.value.filterNot { it.catalogedWordId == wordId }
    }

    override suspend fun clearAllWords() {
        wordsState.value = emptyList()
    }

    override suspend fun clearAllMeanings() {
        meaningsState.value = emptyList()
    }

    override fun observeAllWords(): Flow<List<String>> = wordsState.map { words -> words.map { it.word } }

    override suspend fun getAllWords(): List<String> = wordsState.value.map { it.word }

    private fun relationFor(entity: CatalogedWordEntity): CatalogedWordWithMeanings {
        return CatalogedWordWithMeanings(
            word = entity,
            meanings = meaningsState.value.filter { it.catalogedWordId == entity.id },
        )
    }

    private fun nextWordId(): Long = (wordsState.value.maxOfOrNull { it.id } ?: 0L) + 1L

    private fun nextMeaningId(current: List<CatalogedMeaningEntity>): Long =
        (current.maxOfOrNull { it.id } ?: 0L) + 1L
}
