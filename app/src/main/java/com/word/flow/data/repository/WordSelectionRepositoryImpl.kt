package com.word.flow.data.repository

import com.word.flow.data.local.db.dao.CatalogedWordDao
import com.word.flow.data.local.db.dao.EncounteredWordDao
import com.word.flow.domain.model.WordSelectionItem
import com.word.flow.domain.model.WordType
import com.word.flow.domain.repository.WordSelectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WordSelectionRepositoryImpl @Inject constructor(
    private val encounteredWordDao: EncounteredWordDao,
    private val catalogedWordDao: CatalogedWordDao,
) : WordSelectionRepository {
    override suspend fun getSelectableWords(): List<WordSelectionItem> {
        val encountered = encounteredWordDao.getAllWords().mapIndexed { idx, word ->
            WordSelectionItem(
                id = idx.toLong() + 1_000_000,
                word = word,
                wordType = WordType.ENCOUNTERED,
            )
        }
        val cataloged = catalogedWordDao.getAllWords().mapIndexed { idx, word ->
            WordSelectionItem(
                id = idx.toLong() + 2_000_000,
                word = word,
                wordType = WordType.CATALOGED,
            )
        }
        return (encountered + cataloged).distinctBy { it.word.lowercase(Locale.getDefault()) }
    }

    override fun observeSelectableWords(): Flow<List<WordSelectionItem>> {
        val encountered = encounteredWordDao.observeAllWords().map { words ->
            words.mapIndexed { idx, word ->
                WordSelectionItem(
                    id = idx.toLong() + 1_000_000,
                    word = word,
                    wordType = WordType.ENCOUNTERED,
                )
            }
        }
        val cataloged = catalogedWordDao.observeAllWords().map { words ->
            words.mapIndexed { idx, word ->
                WordSelectionItem(
                    id = idx.toLong() + 2_000_000,
                    word = word,
                    wordType = WordType.CATALOGED,
                )
            }
        }
        return combine(encountered, cataloged) { e, c ->
            (e + c).distinctBy { it.word.lowercase(Locale.getDefault()) }
        }
    }
}