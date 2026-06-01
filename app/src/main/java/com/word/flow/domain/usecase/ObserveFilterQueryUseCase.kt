package com.word.flow.domain.usecase

import com.word.flow.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveFilterQueryUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
) {
    operator fun invoke(): Flow<String> = preferencesRepository.filterQuery
}