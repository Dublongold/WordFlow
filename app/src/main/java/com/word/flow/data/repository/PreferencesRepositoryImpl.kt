package com.word.flow.data.repository

import com.word.flow.data.local.datastore.AppPreferences
import com.word.flow.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepositoryImpl @Inject constructor(
    private val appPreferences: AppPreferences,
) : PreferencesRepository {
    override val filterQuery: Flow<String> = appPreferences.filterQuery

    override suspend fun setFilterQuery(query: String) = appPreferences.setFilterQuery(query)
}