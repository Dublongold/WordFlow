package com.word.flow.data.local.datastore

import kotlinx.coroutines.flow.Flow

interface AppPreferences {
    val wordsSeeded: Flow<Boolean>
    val filterQuery: Flow<String>

    suspend fun setWordsSeeded(value: Boolean)
    suspend fun setFilterQuery(query: String)
}
