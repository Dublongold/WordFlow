package com.word.flow.domain.usecase

import com.word.flow.domain.repository.EncounteredWordRepository
import javax.inject.Inject

class ClearViewedWordsUseCase @Inject constructor(
    private val encounteredWordRepository: EncounteredWordRepository,
) {
    suspend operator fun invoke() = encounteredWordRepository.clearAll()
}