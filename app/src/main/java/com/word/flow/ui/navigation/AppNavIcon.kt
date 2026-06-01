package com.word.flow.ui.navigation

import com.word.flow.R

sealed interface AppNavIcon {
    companion object {
        val items = listOf(
            Home,
            WordNotes,
            ViewedNotes,
            Settings,
        )
    }

    val label: String
    val icon: Int
    val destination: AppRoute

    data object Home : AppNavIcon {
        override val label = "Home"
        override val icon = R.drawable.ic_home
        override val destination = AppRoute.Home
    }

    data object WordNotes : AppNavIcon {
        override val label = "Word Notes"
        override val icon = R.drawable.ic_word_notes
        override val destination = AppRoute.Notes
    }

    data object ViewedNotes : AppNavIcon {
        override val label = "Viewed"
        override val icon = R.drawable.ic_viewed_words
        override val destination = AppRoute.Viewed
    }

    data object Settings : AppNavIcon {
        override val label = "Settings"
        override val icon = R.drawable.ic_settings
        override val destination = AppRoute.Settings
    }
}