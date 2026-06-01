package com.word.flow.ui.navigation

import androidx.navigation3.runtime.NavKey
import com.word.flow.domain.model.WordType
import kotlinx.serialization.Serializable

@Serializable
sealed class AppRoute(val isSubRoute: Boolean = false, val haveHistory: Boolean = false) : NavKey {
    abstract val title: String
    @Serializable
    data object Home : AppRoute() {
        override val title = "Home"
        @Serializable
        data class Dictionary(val presetWord: String? = null) : AppRoute(isSubRoute = true) {
            override val title = TITLE
            companion object {
                const val TITLE = "Dictionary"
            }
        }
        @Serializable
        data object Swiping : AppRoute(isSubRoute = true) {
            override val title = "Swiping"
        }

        @Serializable
        data object Counter : AppRoute(isSubRoute = true, haveHistory = true) {
            override val title = "Word Counter"
        }

        @Serializable
        data object LetterCalculator : AppRoute(isSubRoute = true, haveHistory = true) {
            override val title = "Letter Calculator"
        }

        @Serializable
        data object GuessWord : AppRoute(isSubRoute = true) {
            override val title = "Guess Word"
        }
    }



    @Serializable
    data object Notes : AppRoute() {
        override val title = "Word Notes"

        @Serializable
        data class Edit(
            val noteId: Long? = null,
            val wordId: Long = -1,
            val wordType: WordType = WordType.ENCOUNTERED,
        ) : AppRoute(isSubRoute = true) {
            override val title = "${if (noteId != null) "Edit" else "Add"} Note"
        }
    }



    @Serializable
    data object Viewed : AppRoute() {
        override val title = "Viewed"
    }

    @Serializable
    data object Favorites : AppRoute(isSubRoute = true) {
        override val title = "Favorites"
    }

    @Serializable
    data object Settings : AppRoute() {
        override val title = "Settings"
    }
}
