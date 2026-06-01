package com.word.flow.domain.model

enum class WordType {
    ENCOUNTERED,
    CATALOGED
}

data class EncounteredWord(
    val id: Long,
    val word: String,
    val isKnown: Boolean,
    val isFavorite: Boolean,
    val swipedAt: Long,
)

data class CatalogedMeaning(
    val partOfSpeech: String,
    val definition: String,
)

data class CatalogedWord(
    val id: Long,
    val word: String,
    val phonetic: String?,
    val origin: String?,
    val meanings: List<CatalogedMeaning>,
    val loadedAt: Long,
)

data class WordNote(
    val id: Long,
    val wordId: Long,
    val wordType: WordType,
    val word: String,
    val note: String,
    val createdAt: Long,
    val updatedAt: Long,
)

data class WordDetail(
    val word: String,
    val phonetic: String?,
    val origin: String?,
    val meanings: List<CatalogedMeaning>,
)

data class WordSelectionItem(
    val id: Long,
    val word: String,
    val wordType: WordType,
)
