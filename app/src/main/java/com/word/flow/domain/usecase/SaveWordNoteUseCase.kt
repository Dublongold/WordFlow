package com.word.flow.domain.usecase

import com.word.flow.domain.model.WordNote
import com.word.flow.domain.repository.WordNoteRepository
import javax.inject.Inject

class SaveWordNoteUseCase @Inject constructor(
    private val wordNoteRepository: WordNoteRepository,
) {
    suspend operator fun invoke(note: WordNote): Long = wordNoteRepository.upsert(note)
}