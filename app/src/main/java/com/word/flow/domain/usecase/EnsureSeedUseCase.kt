package com.word.flow.domain.usecase

import com.word.flow.domain.repository.SeedRepository
import javax.inject.Inject

class EnsureSeedUseCase @Inject constructor(
    private val seedRepository: SeedRepository,
) {
    suspend operator fun invoke() = seedRepository.ensureSeeded()
}