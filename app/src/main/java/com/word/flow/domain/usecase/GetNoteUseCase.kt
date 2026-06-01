package com.word.flow.domain.usecase

import com.word.flow.domain.repository.WordNoteRepository
import javax.inject.Inject

class GetNoteUseCase @Inject constructor(
    private val wordNoteRepository: WordNoteRepository,
) {
    suspend operator fun invoke(id: Long) = wordNoteRepository.getById(id)
    suspend operator fun invoke() = wordNoteRepository.getAll()
}