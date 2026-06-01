package com.word.flow.data.local.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cataloged_meanings",
    foreignKeys = [
        ForeignKey(
            entity = CatalogedWordEntity::class,
            parentColumns = ["id"],
            childColumns = ["catalogedWordId"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [Index("catalogedWordId")],
)
data class CatalogedMeaningEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val catalogedWordId: Long,
    val partOfSpeech: String,
    val definition: String,
)