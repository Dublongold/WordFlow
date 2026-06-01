package com.word.flow.data.remote.api

import com.word.flow.data.remote.dto.DictionaryResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApi {
    @GET("api/v2/entries/en/{content}")
    suspend fun getWord(@Path("content") word: String): List<DictionaryResponseDto>
}
