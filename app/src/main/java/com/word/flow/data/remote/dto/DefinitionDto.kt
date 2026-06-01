package com.word.flow.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DefinitionDto(
    val definition: String,
    val example: String? = null,
)
