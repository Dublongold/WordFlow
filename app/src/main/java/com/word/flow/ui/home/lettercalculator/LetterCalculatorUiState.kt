package com.word.flow.ui.home.lettercalculator

data class LetterCalculatorUiState(
    val text: String = "",
    val letterCount: Int = 0,
    val lettersFrequency: List<Pair<Char, Int>> = emptyList(),
)