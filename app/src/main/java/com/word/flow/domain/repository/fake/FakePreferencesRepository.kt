package com.word.flow.domain.repository.fake

import com.word.flow.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakePreferencesRepository(
    initialQuery: String = "",
) : PreferencesRepository {

    private val filterState = MutableStateFlow(initialQuery)

    override val filterQuery: Flow<String> = filterState

    override suspend fun setFilterQuery(query: String) {
        filterState.value = query
    }
}
