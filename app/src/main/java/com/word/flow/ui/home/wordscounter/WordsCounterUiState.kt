package com.word.flow.ui.home.wordscounter

data class WordsCounterUiState(
    val text: String = "",
    val result: Int = 0,
    val wordsMap: Map<String, Int> = emptyMap(),
)