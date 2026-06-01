package com.word.flow.domain.usecase

import com.word.flow.domain.repository.EncounteredWordRepository
import javax.inject.Inject

class SwipeWordUseCase @Inject constructor(
    private val encounteredWordRepository: EncounteredWordRepository,
) {
    suspend operator fun invoke(word: String, isKnown: Boolean) =
        encounteredWordRepository.upsert(word = word, isKnown = isKnown)
}