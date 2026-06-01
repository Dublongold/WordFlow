package com.word.flow.domain.repository.fake

import com.word.flow.domain.model.WordSelectionItem
import com.word.flow.domain.repository.WordSelectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Locale

@Suppress("unused")
class FakeWordSelectionRepository(
    initialItems: List<WordSelectionItem> = emptyList(),
) : WordSelectionRepository {

    private val items = MutableStateFlow(initialItems)

    override suspend fun getSelectableWords(): List<WordSelectionItem> = items.value

    override fun observeSelectableWords(): Flow<List<WordSelectionItem>> = items

    fun setItems(newItems: List<WordSelectionItem>) {
        items.value = newItems.distinctBy { it.word.lowercase(Locale.getDefault()) }
    }

    fun add(item: WordSelectionItem) {
        setItems(items.value + item)
    }
}
