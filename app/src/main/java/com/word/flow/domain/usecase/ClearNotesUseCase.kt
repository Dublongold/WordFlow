package com.word.flow.domain.usecase

import com.word.flow.domain.repository.WordNoteRepository
import javax.inject.Inject

class ClearNotesUseCase @Inject constructor(
    private val wordNoteRepository: WordNoteRepository,
) {
    suspend operator fun invoke() = wordNoteRepository.clearAll()
}