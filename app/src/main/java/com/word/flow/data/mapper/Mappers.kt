package com.word.flow.data.mapper

import com.word.flow.data.local.db.entity.CatalogedMeaningEntity
import com.word.flow.data.local.db.entity.CatalogedWordEntity
import com.word.flow.data.local.db.entity.CatalogedWordWithMeanings
import com.word.flow.data.local.db.entity.EncounteredWordEntity
import com.word.flow.data.local.db.entity.WordNoteEntity
import com.word.flow.data.remote.dto.DictionaryResponseDto
import com.word.flow.domain.model.CatalogedMeaning
import com.word.flow.domain.model.CatalogedWord
import com.word.flow.domain.model.EncounteredWord
import com.word.flow.domain.model.WordNote
import com.word.flow.domain.model.WordType

fun EncounteredWordEntity.toDomain() = EncounteredWord(
    id = id,
    word = word,
    isKnown = isKnown,
    isFavorite = isFavorite,
    swipedAt = swipedAt,
)

fun WordNoteEntity.toDomain() = WordNote(
    id = id,
    wordId = wordId,
    wordType = WordType.valueOf(wordType),
    word = word,
    note = note,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun WordNote.toEntity(word: String) = WordNoteEntity(
    id = id,
    wordId = wordId,
    wordType = wordType.name,
    word = word,
    note = note,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun CatalogedWordWithMeanings.toDomain() = CatalogedWord(
    id = word.id,
    word = word.word,
    phonetic = word.phonetic,
    origin = word.origin,
    meanings = meanings.map {
        CatalogedMeaning(
            partOfSpeech = it.partOfSpeech,
            definition = it.definition,
        )
    },
    loadedAt = word.loadedAt,
)

fun DictionaryResponseDto.toWordAndMeanings(now: Long): Pair<CatalogedWordEntity, List<CatalogedMeaningEntity>> {
    val entity = CatalogedWordEntity(
        word = word,
        phonetic = phonetic ?: phonetics.firstOrNull { !it.text.isNullOrBlank() }?.text,
        origin = origin,
        loadedAt = now,
    )

    val meanings = meanings.mapNotNull { meaning ->
        val def = meaning.definitions.firstOrNull()?.definition ?: return@mapNotNull null
        CatalogedMeaningEntity(
            catalogedWordId = 0,
            partOfSpeech = meaning.partOfSpeech,
            definition = def,
        )
    }

    return entity to meanings
}
