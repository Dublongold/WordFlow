package com.word.flow.domain.usecase

import com.word.flow.data.local.db.dao.CountersDao
import com.word.flow.data.local.db.entity.LetterHistoryEntity
import com.word.flow.data.local.db.entity.WordHistoryEntity
import javax.inject.Inject

class UpsertCountersUseCase @Inject constructor(
    private val countersDao: CountersDao,
) {
    suspend operator fun invoke(wordHistoryEntity: WordHistoryEntity) = countersDao.upsertWordHistory(wordHistoryEntity)
    suspend operator fun invoke(letterHistoryEntity: LetterHistoryEntity) = countersDao.upsertLetterHistory(letterHistoryEntity)
}