package com.word.flow.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val filterQuery: Flow<String>
    suspend fun setFilterQuery(query: String)
}