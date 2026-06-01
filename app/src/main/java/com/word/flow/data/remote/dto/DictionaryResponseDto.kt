package com.word.flow.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DictionaryResponseDto(
    val word: String,
    val phonetic: String? = null,
    val phonetics: List<PhoneticDto> = emptyList(),
    val meanings: List<MeaningDto> = emptyList(),
    val origin: String? = null,
)