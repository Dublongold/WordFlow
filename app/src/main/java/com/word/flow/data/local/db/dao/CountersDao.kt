package com.word.flow.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.word.flow.data.local.db.entity.LetterHistoryEntity
import com.word.flow.data.local.db.entity.WordHistoryEntity
import com.word.flow.data.local.db.model.ShortWordHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CountersDao {
    @Query("SELECT * FROM word_history_entity")
    fun observeFullWordHistory(): Flow<List<WordHistoryEntity>>
    @Query("SELECT id, CASE\n" +
            "        WHEN count > 100\n" +
            "        THEN SUBSTR(content, 1, 100) || '…'\n" +
            "        ELSE content\n" +
            "    END AS content, " +
            "count " +
            "FROM word_history_entity")
    fun observeTruncatedFullWordHistory(): Flow<List<ShortWordHistoryEntity>>
    @Query("SELECT * FROM word_history_entity WHERE id = :id")
    suspend fun getWordHistoryById(id: Long): WordHistoryEntity?

    @Query("SELECT * FROM letter_history_entity")
    fun observerFullLetterHistory(): Flow<List<LetterHistoryEntity>>

    @Upsert
    suspend fun upsertWordHistory(wordHistoryEntity: WordHistoryEntity)

    @Upsert
    suspend fun upsertLetterHistory(letterHistoryEntity: LetterHistoryEntity)

    @Delete
    suspend fun deleteWordHistory(wordHistoryEntity: WordHistoryEntity)

    @Delete
    suspend fun deleteLetterHistory(letterHistoryEntity: LetterHistoryEntity)

    @Query("DELETE FROM word_history_entity WHERE id = :id")
    suspend fun deleteWordHistoryById(id: Long)

    @Query("DELETE FROM letter_history_entity WHERE id = :id")
    suspend fun deleteLetterHistoryById(id: Long)
}