package com.word.flow.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.word.flow.data.local.db.entity.WordNoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WordNoteDao {
    @Query(
        """
            SELECT * FROM word_notes
            WHERE (:selectedWord IS NULL OR word = :selectedWord)
            AND (
                :query = '' OR
                word LIKE :query OR
                note LIKE :query
            )
            ORDER BY updatedAt DESC
        """
    )
    fun observeNotes(query: String, selectedWord: String?): Flow<List<WordNoteEntity>>

    @Query("SELECT * FROM word_notes")
    suspend fun getAll(): List<WordNoteEntity>

    @Query("SELECT * FROM word_notes WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): WordNoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: WordNoteEntity): Long

    @Query("DELETE FROM word_notes WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM word_notes")
    suspend fun clearAll()
}