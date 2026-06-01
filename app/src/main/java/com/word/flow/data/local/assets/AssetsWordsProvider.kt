package com.word.flow.data.local.assets

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssetsWordsProvider @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    val words: List<String> by lazy {
        getWordsFromAssets()
    }

    fun getWordsFromAssets(): List<String> {
        val content = context.assets.open("words.json").bufferedReader().use { it.readText() }
        return Json.decodeFromString<List<String>>(content)
            .map { it.trim().lowercase(Locale.getDefault()) }.filter { it.isNotBlank() }.distinct()
    }
}