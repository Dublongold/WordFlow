package com.word.flow.domain.usecase

import com.word.flow.domain.repository.EncounteredWordRepository
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetViewedWordsUseCase @Inject constructor(
    private val encounteredWordRepository: EncounteredWordRepository,
) {
    fun count() = combine(
        encounteredWordRepository.observeKnownCount(),
        encounteredWordRepository.observeUnknownCount(),
    ) { known, unknown ->
        known + unknown
    }
    fun favoriteCount() = combine(
        encounteredWordRepository.observeFavoriteKnownCount(),
        encounteredWordRepository.observeFavoriteUnknownCount(),
    ) { known, unknown ->
        known + unknown
    }
    operator fun invoke(isKnown: Boolean, query: String) = encounteredWordRepository.observeEncountered(isKnown, query)

    fun favorites(isKnown: Boolean, query: String) = encounteredWordRepository.observeFavorites(isKnown, query)

    suspend fun setFavorite(word: String, isFavorite: Boolean) = encounteredWordRepository.setFavorite(word, isFavorite)
}