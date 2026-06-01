package com.word.flow.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "word_history_entity",
    indices = [Index(value = ["content", "count"], unique = true)]
)
data class WordHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val content: String,
    val count: Int,
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    val createdAt: Long = System.currentTimeMillis(),
)