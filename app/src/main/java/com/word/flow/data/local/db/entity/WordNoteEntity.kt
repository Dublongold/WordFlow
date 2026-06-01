package com.word.flow.data.local.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "word_notes", indices = [Index(value = ["word"], unique = true)])
data class WordNoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val wordId: Long,
    val wordType: String,
    val word: String,
    val note: String,
    val createdAt: Long,
    val updatedAt: Long,
)