package com.word.flow.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.word.flow.R
import com.word.flow.ui.home.HomeScreen
import com.word.flow.ui.home.dictionary.DictionaryScreen
import com.word.flow.ui.home.guessword.GuessWordScreen
import com.word.flow.ui.home.lettercalculator.LetterCalculatorScreen
import com.word.flow.ui.home.swiping.SwipingScreen
import com.word.flow.ui.home.wordscounter.WordsCounterScreen
import com.word.flow.ui.notes.NoteEditScreen
import com.word.flow.ui.notes.NotesScreen
import com.word.flow.ui.settings.SettingsScreen
import com.word.flow.ui.viewed.ViewedScreen
import com.word.flow.ui.viewed.favorites.FavoritesScreen

@Composable
fun AppNavGraphContent(
    backStack: NavBackStack<NavKey>,
    snackbarHostState: SnackbarHostState? = null,
) {
    val historyManager = LocalHistoryManager.current
    Image(
        painterResource(R.drawable.bg_app),
        null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
    )
    NavDisplay(
        backStack = backStack,
        modifier = Modifier.fillMaxSize(),
    ) { entry ->
        NavEntry(entry) { navKey ->
            when (navKey) {
                AppRoute.Home -> {
                    HomeScreen(
                        onDictionary = { word -> backStack.add(AppRoute.Home.Dictionary(word)) },
                        onSwiping = { backStack.add(AppRoute.Home.Swiping) },
                        onCounter = { backStack.add(AppRoute.Home.Counter) },
                        onLetterCalculator = { backStack.add(AppRoute.Home.LetterCalculator) },
                        onGuessWord = { backStack.add(AppRoute.Home.GuessWord) },
                        snackbarHostState = snackbarHostState
                    )
                }

                is AppRoute.Home.Dictionary -> {
                    DictionaryScreen(presetWord = navKey.presetWord)
                }

                is AppRoute.Home.Swiping -> {
                    SwipingScreen()
                }

                is AppRoute.Notes -> {
                    NotesScreen(
                        onAdd = { backStack.add(AppRoute.Notes.Edit()) },
                        onEdit = { noteId -> backStack.add(AppRoute.Notes.Edit(noteId = noteId)) },
                    )
                }

                is AppRoute.Notes.Edit -> {
                    NoteEditScreen(
                        noteId = navKey.noteId,
                        wordId = navKey.wordId,
                        wordType = navKey.wordType,
                        onDone = { if (backStack.size > 1) backStack.removeLastOrNull() },
                    )
                }

                is AppRoute.Home.Counter -> {
                    historyManager.BackHandler()
                    WordsCounterScreen()
                }

                is AppRoute.Home.LetterCalculator -> {
                    historyManager.BackHandler()
                    LetterCalculatorScreen()
                }

                is AppRoute.Home.GuessWord -> {
                    GuessWordScreen()
                }

                is AppRoute.Viewed -> {
                    ViewedScreen(
                        onSelectWord = { word -> backStack.add(AppRoute.Home.Dictionary(word)) },
                    )
                }

                is AppRoute.Favorites -> {
                    FavoritesScreen(
                        onSelectWord = { word -> backStack.add(AppRoute.Home.Dictionary(word)) },
                    )
                }

                is AppRoute.Settings -> {
                    SettingsScreen()
                }
            }
        }
    }
}

@Preview
@Composable
private fun AppNavGraphContentPreview() {
    AppNavGraphContent(
        backStack = rememberNavBackStack(AppRoute.Home),
    )
}