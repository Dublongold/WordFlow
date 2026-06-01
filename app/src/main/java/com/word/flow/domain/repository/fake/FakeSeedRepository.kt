package com.word.flow.domain.repository.fake

import com.word.flow.domain.repository.SeedRepository

@Suppress("unused")
class FakeSeedRepository(
    private val pool: MutableList<String> = mutableListOf(),
) : SeedRepository {

    private var seeded = false

    override suspend fun ensureSeeded() {
        seeded = true
    }

    override suspend fun getSwipePool(): List<String> = if (seeded) pool.toList() else emptyList()

    fun setSeeded(value: Boolean) {
        seeded = value
    }

    fun addToPool(word: String) {
        pool += word
    }
}
