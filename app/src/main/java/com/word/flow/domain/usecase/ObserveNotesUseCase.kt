package com.word.flow.domain.usecase

import com.word.flow.domain.repository.WordNoteRepository
import javax.inject.Inject

class ObserveNotesUseCase @Inject constructor(
    private val wordNoteRepository: WordNoteRepository,
) {
    operator fun invoke(query: String, selectedWord: String?) = wordNoteRepository.observeNotes(query, selectedWord)
}