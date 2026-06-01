package com.word.flow.data.local.datastore

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeAppPreferences(
    initialWordsSeeded: Boolean = false,
    initialFilterQuery: String = "",
) : AppPreferences {
    private val wordsSeededState = MutableStateFlow(initialWordsSeeded)
    private val filterQueryState = MutableStateFlow(initialFilterQuery)

    override val wordsSeeded: Flow<Boolean> = wordsSeededState
    override val filterQuery: Flow<String> = filterQueryState

    override suspend fun setWordsSeeded(value: Boolean) {
        wordsSeededState.value = value
    }

    override suspend fun setFilterQuery(query: String) {
        filterQueryState.value = query
    }
}
