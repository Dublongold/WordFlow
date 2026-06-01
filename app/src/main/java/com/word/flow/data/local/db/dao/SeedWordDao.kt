package com.word.flow.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.word.flow.data.local.db.entity.SeedWordEntity

@Dao
interface SeedWordDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(words: List<SeedWordEntity>)

    @Query("SELECT COUNT(*) FROM seed_words")
    suspend fun count(): Int

    @Query(
        """
            SELECT word FROM seed_words
            WHERE word NOT IN (SELECT word FROM encountered_words)
            ORDER BY id ASC
        """
    )
    suspend fun getSwipePool(): List<String>
}
