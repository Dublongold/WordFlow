package com.word.flow.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PhoneticDto(
    val text: String? = null,
    val audio: String? = null,
)