package com.word.flow.domain.usecase

import com.word.flow.domain.model.WordDetail
import com.word.flow.domain.repository.CatalogedWordRepository
import com.word.flow.domain.repository.EncounteredWordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveHomeUseCase @Inject constructor(
    private val encounteredWordRepository: EncounteredWordRepository,
    private val catalogedWordRepository: CatalogedWordRepository,
) {
    data class HomeData(
        val knownCount: Int,
        val unknownCount: Int,
        val randomWord: WordDetail?,
    )

    operator fun invoke(): Flow<HomeData> = combine(
        encounteredWordRepository.observeKnownCount(),
        encounteredWordRepository.observeUnknownCount(),
        catalogedWordRepository.observeRandom(),
    ) { known, unknown, random ->
        HomeData(
            knownCount = known,
            unknownCount = unknown,
            randomWord = random?.let {
                WordDetail(
                    word = it.word,
                    phonetic = it.phonetic,
                    origin = it.origin,
                    meanings = it.meanings,
                )
            },
        )
    }
}
