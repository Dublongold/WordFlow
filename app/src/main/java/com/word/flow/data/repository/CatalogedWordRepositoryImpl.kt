package com.word.flow.data.repository

import com.word.flow.data.local.db.dao.CatalogedWordDao
import com.word.flow.data.mapper.toDomain
import com.word.flow.data.mapper.toWordAndMeanings
import com.word.flow.data.remote.api.DictionaryApi
import com.word.flow.domain.model.CatalogedWord
import com.word.flow.domain.repository.CatalogedWordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatalogedWordRepositoryImpl @Inject constructor(
    private val dao: CatalogedWordDao,
    private val api: DictionaryApi,
) : CatalogedWordRepository {
    override suspend fun getByWord(word: String): CatalogedWord? = dao.getByWord(word.lowercase(
        Locale.getDefault()))?.toDomain()

    override suspend fun fetchAndCache(word: String): Result<CatalogedWord> {
        val normalized = word.trim().lowercase(Locale.getDefault())
        return runCatching {
            val cached = getByWord(normalized)
            if (cached != null) return@runCatching cached

            val dto = api.getWord(normalized).firstOrNull() ?: throw Exception()
            val now = System.currentTimeMillis()
            val (wordEntity, meanings) = dto.toWordAndMeanings(now)
            val id = dao.insertWord(wordEntity.copy(word = normalized))
            dao.clearMeanings(id)
            dao.insertMeanings(meanings.map { it.copy(catalogedWordId = id) })
            dao.getByWord(normalized)?.toDomain() ?: error("Failed to read cached content")
        }
    }

    override fun observeRandom(): Flow<CatalogedWord?> = dao.observeAllWords().map {
        it.randomOrNull()?.let { dao.getByWord(it)?.toDomain() }
    }

    override suspend fun clearAll() {
        dao.clearAllMeanings()
        dao.clearAllWords()
    }

    override suspend fun getAllWords(): List<String> = dao.getAllWords()
}