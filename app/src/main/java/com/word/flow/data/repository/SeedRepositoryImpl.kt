package com.word.flow.data.repository

import android.content.Context
import com.word.flow.data.local.datastore.AppPreferences
import com.word.flow.data.local.db.dao.SeedWordDao
import com.word.flow.data.local.db.entity.SeedWordEntity
import com.word.flow.domain.repository.SeedRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeedRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val seedDao: SeedWordDao,
    private val preferences: AppPreferences,
) : SeedRepository {
    override suspend fun ensureSeeded() {
        val seeded = preferences.wordsSeeded.first()
        if (seeded) return

        if (seedDao.count() == 0) {
            val content = context.assets.open("words.json").bufferedReader().use { it.readText() }
            val words = Json.decodeFromString<List<String>>(content)
                .map { it.trim().lowercase(Locale.getDefault()) }
                .filter { it.isNotBlank() }
                .distinct()
            seedDao.insertAll(words.map { SeedWordEntity(word = it) })
        }
        preferences.setWordsSeeded(true)
    }

    override suspend fun getSwipePool(): List<String> = seedDao.getSwipePool()
}