package com.word.flow.data.local.db.model

import com.word.flow.ui.home.wordscounter.history.WordsCounterHistoryItem

data class ShortWordHistoryEntity(
    val id: Long = 0,
    val content: String,
    val count: Int,
) {
    fun toWordsCounterHistoryItem(): WordsCounterHistoryItem {
        return WordsCounterHistoryItem(
            id = id,
            content = content,
            count = count,
        )
    }
}