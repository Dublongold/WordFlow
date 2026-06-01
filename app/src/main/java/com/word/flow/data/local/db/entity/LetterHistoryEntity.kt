package com.word.flow.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "letter_history_entity"
)
data class LetterHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val content: String,
)