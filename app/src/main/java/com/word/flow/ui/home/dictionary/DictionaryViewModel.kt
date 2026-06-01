package com.word.flow.ui.home.dictionary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.word.flow.domain.model.CatalogedWord
import com.word.flow.domain.usecase.SearchWordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

sealed interface DictionaryError {
    object NoWordFound : DictionaryError
    object NoNetwork : DictionaryError
}

data class DictionaryUiState(
    val query: String = "",
    val loading: Boolean = false,
    val word: CatalogedWord? = null,
    val error: DictionaryError? = null,
) {
}

@HiltViewModel
class DictionaryViewModel @Inject constructor(
    private val searchWordUseCase: SearchWordUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(DictionaryUiState())
    val uiState: StateFlow<DictionaryUiState> = _uiState.asStateFlow()

    fun setQuery(value: String) {
        _uiState.value = _uiState.value.copy(query = value, error = null)
    }

    fun search(word: String = _uiState.value.query) {
        if (word.isBlank()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)
            val result = searchWordUseCase(word)
            _uiState.value = result.fold(
                onSuccess = {
                    _uiState.value.copy(
                        loading = false,
                        word = it,
                        error = null,
                        query = word
                    )
                },
                onFailure = {
                    _uiState.value.copy(
                        loading = false,
                        error = if (it !is IOException && it.message != null) {
                            DictionaryError.NoWordFound
                        } else {
                            DictionaryError.NoNetwork
                        }
                    )
                },
            )
        }
    }
}
