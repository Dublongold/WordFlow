package com.word.flow.domain.repository

interface SeedRepository {
    suspend fun ensureSeeded()
    suspend fun getSwipePool(): List<String>
}