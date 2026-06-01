package com.word.flow.domain.usecase

import javax.inject.Inject

class CountWordsUseCase @Inject constructor() {
    operator fun invoke(text: String): Int = text
        .trim()
        .split(Regex("\\s+"))
        .filter { it.isNotBlank() }
        .size

    fun wordCountMap(text: String): Map<String, Int> {
        return text.lowercase()
            .split(Regex("\\W+"))
            .filter { it.isNotEmpty() }
            .groupingBy { it }
            .eachCount()
            .toList()
            .sortedByDescending {
                it.second
            }.toMap()
    }
}