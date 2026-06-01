package com.word.flow.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.word.flow.domain.usecase.EnsureSeedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AppInitializer @Inject constructor(
    private val ensureSeedUseCase: EnsureSeedUseCase,
) : ViewModel() {
    fun ensureSeeded() {
        viewModelScope.launch {
            ensureSeedUseCase()
        }
    }
}
