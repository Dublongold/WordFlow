package com.word.flow.data.local.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "seed_words", indices = [Index(value = ["word"], unique = true)])
data class SeedWordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val word: String,
)
