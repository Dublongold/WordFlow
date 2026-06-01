package com.word.flow.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.word.flow.data.local.db.dao.CatalogedWordDao
import com.word.flow.data.local.db.dao.CountersDao
import com.word.flow.data.local.db.dao.EncounteredWordDao
import com.word.flow.data.local.db.dao.SeedWordDao
import com.word.flow.data.local.db.dao.WordNoteDao
import com.word.flow.data.local.db.entity.CatalogedMeaningEntity
import com.word.flow.data.local.db.entity.CatalogedWordEntity
import com.word.flow.data.local.db.entity.EncounteredWordEntity
import com.word.flow.data.local.db.entity.LetterHistoryEntity
import com.word.flow.data.local.db.entity.SeedWordEntity
import com.word.flow.data.local.db.entity.WordHistoryEntity
import com.word.flow.data.local.db.entity.WordNoteEntity

@Database(
    entities = [
        EncounteredWordEntity::class,
        CatalogedWordEntity::class,
        CatalogedMeaningEntity::class,
        WordNoteEntity::class,
        SeedWordEntity::class,
        LetterHistoryEntity::class,
        WordHistoryEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun encounteredWordDao(): EncounteredWordDao
    abstract fun catalogedWordDao(): CatalogedWordDao
    abstract fun wordNoteDao(): WordNoteDao
    abstract fun seedWordDao(): SeedWordDao
    abstract fun countersDao(): CountersDao
}
