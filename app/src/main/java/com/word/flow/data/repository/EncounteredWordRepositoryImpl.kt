package com.word.flow.data.repository

import com.word.flow.data.local.db.dao.EncounteredWordDao
import com.word.flow.data.local.db.entity.EncounteredWordEntity
import com.word.flow.data.mapper.toDomain
import com.word.flow.domain.repository.EncounteredWordRepository
import com.word.flow.domain.model.EncounteredWord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EncounteredWordRepositoryImpl @Inject constructor(
    private val dao: EncounteredWordDao,
) : EncounteredWordRepository {
    override fun observeKnownCount(): Flow<Int> = dao.observeKnownCount()

    override fun observeUnknownCount(): Flow<Int> = dao.observeUnknownCount()

    override fun observeFavoriteKnownCount(): Flow<Int> = dao.observeFavoriteKnownCount()

    override fun observeFavoriteUnknownCount(): Flow<Int> = dao.observeFavoriteUnknownCount()

    override fun observeEncountered(isKnown: Boolean, query: String) =
        dao.observeEncountered(isKnown, query.toSqlLike()).map { list -> list.map { it.toDomain() } }

    override fun observeFavorites(isKnown: Boolean, query: String): Flow<List<EncounteredWord>> =
        dao.observeFavorites(isKnown, query.toSqlLike()).map { list -> list.map { it.toDomain() } }

    override suspend fun upsert(word: String, isKnown: Boolean) {
        val existing = dao.getByWord(word)
        dao.insert(
            EncounteredWordEntity(
                id = existing?.id ?: 0,
                word = word,
                isKnown = isKnown,
                isFavorite = existing?.isFavorite ?: false,
                swipedAt = System.currentTimeMillis(),
            )
        )
    }

    override suspend fun setFavorite(word: String, isFavorite: Boolean) {
        dao.setFavorite(word = word, isFavorite = isFavorite)
    }

    override suspend fun clearAll() = dao.clearAll()

    override suspend fun getAllWords(): List<String> = dao.getAllWords()
}