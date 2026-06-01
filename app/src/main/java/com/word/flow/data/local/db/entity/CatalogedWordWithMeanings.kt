package com.word.flow.data.local.db.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CatalogedWordWithMeanings(
    @Embedded val word: CatalogedWordEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "catalogedWordId",
    )
    val meanings: List<CatalogedMeaningEntity>,
)
