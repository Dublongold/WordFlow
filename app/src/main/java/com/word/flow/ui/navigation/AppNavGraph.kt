package com.word.flow.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.rememberNavBackStack
import com.word.flow.ui.AppInitializer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavGraph() {
    val backStack = rememberNavBackStack(AppRoute.Home)
    val initializer: AppInitializer = hiltViewModel()

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) { initializer.ensureSeeded() }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            val currentEntry = backStack.lastOrNull() as? AppRoute
            for (item in AppNavIcon.items) {
                item(
                    selected = when (item) {
                        AppNavIcon.Settings, AppNavIcon.ViewedNotes,
                        AppNavIcon.WordNotes -> currentEntry == item.destination

                        else -> when (currentEntry) {
                            is AppRoute.Home.Dictionary, is AppRoute.Home.Swiping,
                            is AppRoute.Home.Counter, is AppRoute.Home.LetterCalculator,
                            is AppRoute.Home.GuessWord, AppRoute.Home -> {
                                item.destination == AppRoute.Home
                            }
                            is AppRoute.Notes.Edit -> item.destination == AppRoute.Notes
                            else -> false
                        }
                    }, onClick = {
                        if (currentEntry != item.destination) {
                            if (item.destination == AppRoute.Home) {
                                var last = backStack.lastOrNull()
                                while (last != AppRoute.Home) {
                                    backStack.remove(last)
                                    last = backStack.lastOrNull()
                                }
                            } else {
                                if (backStack.size > 1 && backStack.contains(item.destination)) {
                                    backStack.remove(item.destination)
                                }
                                backStack.add(item.destination)
                            }
                        }
                    }, label = { Text(item.label) }, icon = {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.label
                        )
                    })
            }
        }
    ) {
        Scaffold(topBar = {
            MainTopAppBar(
                backStack = backStack,
                onBackClicked = backStack::removeLastOrNull,
            )
        }, snackbarHost = { SnackbarHost(snackbarHostState) }) {
            CompositionLocalProvider(LocalAppPadding provides it) {
                AppNavGraphContent(
                    backStack = backStack,
                    snackbarHostState = snackbarHostState
                )
            }
        }
    }
}
