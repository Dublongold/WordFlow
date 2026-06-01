package com.word.flow.domain.usecase

import com.word.flow.domain.repository.CatalogedWordRepository
import javax.inject.Inject

class ClearCatalogedWordsUseCase @Inject constructor(
    private val catalogedWordRepository: CatalogedWordRepository,
) {
    suspend operator fun invoke() = catalogedWordRepository.clearAll()
}