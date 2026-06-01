package com.word.flow.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class AppPreferencesImpl(
    private val dataStore: DataStore<Preferences>,
) : AppPreferences {
    override val wordsSeeded: Flow<Boolean> = dataStore.data
        .catch {
            if (it is IOException) emit(emptyPreferences()) else throw it
        }
        .map { it[WORDS_SEEDED] ?: false }

    override val filterQuery: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) emit(emptyPreferences()) else throw it
        }
        .map { it[FILTER_QUERY] ?: "" }

    override suspend fun setWordsSeeded(value: Boolean) {
        dataStore.edit { it[WORDS_SEEDED] = value }
    }

    override suspend fun setFilterQuery(query: String) {
        dataStore.edit { it[FILTER_QUERY] = query }
    }

    private companion object {
        val WORDS_SEEDED = booleanPreferencesKey("words_seeded")
        val FILTER_QUERY = stringPreferencesKey("word_filter_query")
    }
}
