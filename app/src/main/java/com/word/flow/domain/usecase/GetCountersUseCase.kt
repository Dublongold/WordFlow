package com.word.flow.domain.usecase

import com.word.flow.data.local.db.dao.CountersDao
import javax.inject.Inject

class GetCountersUseCase @Inject constructor(
    private val countersDao: CountersDao,
) {
    fun observeTruncatedWordHistory() = countersDao.observeTruncatedFullWordHistory()
    suspend fun getWordHistoryById(id: Long) = countersDao.getWordHistoryById(id)
    fun observeLetterHistory() = countersDao.observerFullLetterHistory()
}