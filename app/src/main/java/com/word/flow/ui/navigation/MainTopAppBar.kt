package com.word.flow.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.word.flow.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    backStack: NavBackStack<NavKey>,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val historyManager = LocalHistoryManager.current
    TopAppBar(title = {
        Text(
            (backStack.lastOrNull() as? AppRoute)?.title
                ?: stringResource(R.string.app_name)
        )
    }, navigationIcon = {
        val showBackButton = (backStack.lastOrNull() as? AppRoute)?.isSubRoute == true
        AnimatedVisibility(
            showBackButton,
            Modifier,
            fadeIn() + expandHorizontally { -it },
            fadeOut() + shrinkHorizontally { -it }
        ) {
            IconButton(onClick = {
                historyManager.leaveHistory {
                    onBackClicked()
                }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Back"
                )
            }
        }
    }, actions = {
        val showFavoritesButton = backStack.lastOrNull() == AppRoute.Viewed
        AnimatedVisibility(
            showFavoritesButton,
            Modifier,
            fadeIn() + expandHorizontally { it },
            fadeOut() + shrinkHorizontally { it }
        ) {
            IconButton(onClick = {
                val current = backStack.lastOrNull()
                if (current != AppRoute.Favorites) backStack.add(AppRoute.Favorites)
            }, enabled = showFavoritesButton) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_favorite),
                    contentDescription = "Favorites"
                )
            }
        }
        val haveHistory = (backStack.lastOrNull() as? AppRoute)?.haveHistory == true
        AnimatedVisibility(
            haveHistory && !historyManager.isHistoryNow,
            Modifier,
            fadeIn() + expandHorizontally { it },
            fadeOut() + shrinkHorizontally { it }
        ) {
            IconButton(
                onClick = historyManager::enterHistory,
                enabled = haveHistory && !historyManager.isHistoryNow
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_history),
                    contentDescription = "Favorites"
                )
            }
        }
    }, modifier = modifier)

}

@Preview
@Composable
private fun MainTopAppBarPreview() {
    val backStack = rememberNavBackStack(AppRoute.Home)
    MainTopAppBar(
        backStack = backStack,
        onBackClicked = backStack::removeLastOrNull,
    )
}