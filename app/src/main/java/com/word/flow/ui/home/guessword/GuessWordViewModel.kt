package com.word.flow.ui.home.guessword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.word.flow.data.local.assets.AssetsWordsProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GuessWordUiState(
    val currentWord: String? = null,
    val maskedWord: String = "",
    val guess: String = "",
    val tries: Int = 0,
    val isGuessed: Boolean = false,
    val hint: String? = null,
)

@HiltViewModel
class GuessWordViewModel @Inject constructor(
    private val assetsWordsProvider: AssetsWordsProvider,
) : ViewModel() {
    private val _uiState = MutableStateFlow(GuessWordUiState())
    val uiState: StateFlow<GuessWordUiState> = _uiState.asStateFlow()

    init {
        restartGame()
    }

    fun onGuessChange(value: String) {
        val oneLetter = value.lastOrNull { it.isLetter() }?.toString().orEmpty()
        _uiState.value = _uiState.value.copy(guess = oneLetter)
    }

    fun submitGuess() {
        val state = _uiState.value
        val word = state.currentWord ?: return
        if (state.isGuessed) return

        val guessedLetter = state.guess.firstOrNull() ?: return
        val nextTries = state.tries + 1
        val isInWord = word.any { it.equals(guessedLetter, ignoreCase = true) }

        val nextMaskedWord = word.mapIndexed { index, char ->
            when {
                !char.isLetter() -> char
                state.maskedWord.getOrNull(index)?.equals(char, ignoreCase = true) == true -> char
                char.equals(guessedLetter, ignoreCase = true) -> char
                else -> '*'
            }
        }.joinToString("")

        val completed = nextMaskedWord.none { it == '*' }

        _uiState.value = state.copy(
            maskedWord = nextMaskedWord,
            guess = "",
            tries = nextTries,
            isGuessed = completed,
            hint = when {
                completed -> null
                isInWord -> "Nice! Letter found."
                else -> "No such letter."
            },
        )
    }

    fun restartGame() {
        viewModelScope.launch {
            val words = assetsWordsProvider.words
            if (words.isEmpty()) {
                _uiState.value = GuessWordUiState(
                    currentWord = null,
                    maskedWord = "",
                    hint = "No words available. Add words in Dictionary first.",
                )
                return@launch
            }

            val nextWord = words.random()
            _uiState.value = GuessWordUiState(
                currentWord = nextWord,
                maskedWord = nextWord.map { char -> if (char.isLetter()) '*' else char }.joinToString(""),
            )
        }
    }
}
