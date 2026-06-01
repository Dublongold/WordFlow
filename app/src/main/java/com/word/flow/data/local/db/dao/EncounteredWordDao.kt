package com.word.flow.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.word.flow.data.local.db.entity.EncounteredWordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EncounteredWordDao {
    @Query("SELECT COUNT(*) FROM encountered_words WHERE isKnown = 1")
    fun observeKnownCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM encountered_words WHERE isKnown = 0")
    fun observeUnknownCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM encountered_words WHERE isKnown = 1 AND isFavorite = 1")
    fun observeFavoriteKnownCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM encountered_words WHERE isKnown = 0 AND isFavorite = 1")
    fun observeFavoriteUnknownCount(): Flow<Int>

    @Query(
        """
            SELECT * FROM encountered_words
            WHERE isKnown = :isKnown
            AND (:query = '' OR word LIKE :query)
            ORDER BY swipedAt DESC
        """
    )
    fun observeEncountered(isKnown: Boolean, query: String): Flow<List<EncounteredWordEntity>>

    @Query(
        """
            SELECT * FROM encountered_words
            WHERE isKnown = :isKnown
            AND isFavorite = 1
            AND (:query = '' OR word LIKE :query)
            ORDER BY swipedAt DESC
        """
    )
    fun observeFavorites(isKnown: Boolean, query: String): Flow<List<EncounteredWordEntity>>

    @Query("SELECT * FROM encountered_words WHERE word = :word LIMIT 1")
    suspend fun getByWord(word: String): EncounteredWordEntity?

    @Query("UPDATE encountered_words SET isFavorite = :isFavorite WHERE LOWER(word) = LOWER(:word)")
    suspend fun setFavorite(word: String, isFavorite: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: EncounteredWordEntity): Long

    @Query("DELETE FROM encountered_words")
    suspend fun clearAll()

    @Query("SELECT word FROM encountered_words")
    fun observeAllWords(): Flow<List<String>>
    @Query("SELECT word FROM encountered_words")
    suspend fun getAllWords(): List<String>
}