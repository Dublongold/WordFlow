package com.word.flow.data.local.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "encountered_words", indices = [Index(value = ["word"], unique = true)])
data class EncounteredWordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val word: String,
    val isKnown: Boolean,
    val isFavorite: Boolean = false,
    val swipedAt: Long,
)