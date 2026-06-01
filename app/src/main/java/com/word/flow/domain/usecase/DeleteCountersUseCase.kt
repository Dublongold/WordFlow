package com.word.flow.domain.usecase

import com.word.flow.data.local.db.dao.CountersDao
import javax.inject.Inject

class DeleteCountersUseCase @Inject constructor(
    private val countersDao: CountersDao,
) {
    suspend fun deleteWordHistory(id: Long) = countersDao.deleteWordHistoryById(id)
    suspend fun deleteLetterHistory(id: Long) = countersDao.deleteLetterHistoryById(id)

}