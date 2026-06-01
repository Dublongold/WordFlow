package com.word.flow.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MeaningDto(
    val partOfSpeech: String,
    val definitions: List<DefinitionDto>,
)