package com.word.flow.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.word.flow.data.local.db.entity.CatalogedMeaningEntity
import com.word.flow.data.local.db.entity.CatalogedWordEntity
import com.word.flow.data.local.db.entity.CatalogedWordWithMeanings
import kotlinx.coroutines.flow.Flow

@Dao
interface CatalogedWordDao {
    @Transaction
    @Query("SELECT * FROM cataloged_words WHERE word = :word LIMIT 1")
    suspend fun getByWord(word: String): CatalogedWordWithMeanings?

    @Query("SELECT id FROM cataloged_words ORDER BY RANDOM() LIMIT 1")
    fun observeRandomId(): Flow<Long?>

    @Transaction
    @Query("SELECT * FROM cataloged_words WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): CatalogedWordWithMeanings?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(entity: CatalogedWordEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeanings(entities: List<CatalogedMeaningEntity>)

    @Query("DELETE FROM cataloged_meanings WHERE catalogedWordId = :wordId")
    suspend fun clearMeanings(wordId: Long)

    @Query("DELETE FROM cataloged_words")
    suspend fun clearAllWords()

    @Query("DELETE FROM cataloged_meanings")
    suspend fun clearAllMeanings()

    @Query("SELECT word FROM cataloged_words")
    fun observeAllWords(): Flow<List<String>>

    @Query("SELECT word FROM cataloged_words")
    suspend fun getAllWords(): List<String>
}