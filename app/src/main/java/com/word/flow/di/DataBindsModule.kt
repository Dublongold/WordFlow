package com.word.flow.di

import com.word.flow.data.repository.CatalogedWordRepositoryImpl
import com.word.flow.data.repository.EncounteredWordRepositoryImpl
import com.word.flow.data.repository.PreferencesRepositoryImpl
import com.word.flow.data.repository.SeedRepositoryImpl
import com.word.flow.data.repository.WordNoteRepositoryImpl
import com.word.flow.data.repository.WordSelectionRepositoryImpl
import com.word.flow.domain.repository.CatalogedWordRepository
import com.word.flow.domain.repository.EncounteredWordRepository
import com.word.flow.domain.repository.PreferencesRepository
import com.word.flow.domain.repository.SeedRepository
import com.word.flow.domain.repository.WordNoteRepository
import com.word.flow.domain.repository.WordSelectionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class DataBindsModule {
    @Binds abstract fun bindEncounteredRepository(impl: EncounteredWordRepositoryImpl): EncounteredWordRepository
    @Binds abstract fun bindCatalogedRepository(impl: CatalogedWordRepositoryImpl): CatalogedWordRepository
    @Binds abstract fun bindWordNoteRepository(impl: WordNoteRepositoryImpl): WordNoteRepository
    @Binds abstract fun bindSeedRepository(impl: SeedRepositoryImpl): SeedRepository
    @Binds abstract fun bindPreferencesRepository(impl: PreferencesRepositoryImpl): PreferencesRepository
    @Binds abstract fun bindWordSelectionRepository(impl: WordSelectionRepositoryImpl): WordSelectionRepository
}
