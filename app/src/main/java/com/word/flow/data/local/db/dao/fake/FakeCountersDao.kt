package com.word.flow.data.local.db.dao.fake

import com.word.flow.data.local.db.dao.CountersDao
import com.word.flow.data.local.db.entity.LetterHistoryEntity
import com.word.flow.data.local.db.entity.WordHistoryEntity
import com.word.flow.data.local.db.model.ShortWordHistoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeCountersDao : CountersDao {
    private val wordHistoryState = MutableStateFlow(emptyList<WordHistoryEntity>())
    private val letterHistoryState = MutableStateFlow(emptyList<LetterHistoryEntity>())

    override fun observeFullWordHistory(): Flow<List<WordHistoryEntity>> =
        wordHistoryState

    override fun observerFullLetterHistory(): Flow<List<LetterHistoryEntity>> =
        letterHistoryState

    override suspend fun getWordHistoryById(id: Long): WordHistoryEntity? {
        return wordHistoryState.value.find { it.id == id }
    }

    override fun observeTruncatedFullWordHistory(): Flow<List<ShortWordHistoryEntity>> {
        return wordHistoryState.map { list ->
            list.map { entity ->
                ShortWordHistoryEntity(
                    id = entity.id,
                    content = if (entity.content.length > 100) {
                        entity.content.take(100) + "..."
                    } else {
                        entity.content
                    },
                    count = entity.content.length,
                )
            }
        }
    }

    override suspend fun upsertWordHistory(wordHistoryEntity: WordHistoryEntity) {
        wordHistoryState.value += wordHistoryEntity
    }

    override suspend fun upsertLetterHistory(letterHistoryEntity: LetterHistoryEntity) {
        letterHistoryState.value += letterHistoryEntity
    }

    override suspend fun deleteWordHistory(wordHistoryEntity: WordHistoryEntity) {
        wordHistoryState.value = wordHistoryState.value.filter { it != wordHistoryEntity }
    }

    override suspend fun deleteLetterHistory(letterHistoryEntity: LetterHistoryEntity) {
        letterHistoryState.value = letterHistoryState.value.filter { it != letterHistoryEntity }
    }

    override suspend fun deleteWordHistoryById(id: Long) {
        wordHistoryState.value = wordHistoryState.value.filterNot { it.id == id }
    }

    override suspend fun deleteLetterHistoryById(id: Long) {
        letterHistoryState.value = letterHistoryState.value.filterNot { it.id == id }
    }
}
