package com.word.flow.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.word.flow.data.local.datastore.AppPreferences
import com.word.flow.data.local.datastore.AppPreferencesImpl
import com.word.flow.data.local.db.AppDatabase
import com.word.flow.data.local.db.dao.CatalogedWordDao
import com.word.flow.data.local.db.dao.CountersDao
import com.word.flow.data.local.db.dao.EncounteredWordDao
import com.word.flow.data.local.db.dao.SeedWordDao
import com.word.flow.data.local.db.dao.WordNoteDao
import com.word.flow.data.remote.api.DictionaryApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataProvidersModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "lexicon.db",
    ).fallbackToDestructiveMigration(true).build()

    @Provides
    fun provideEncounteredWordDao(db: AppDatabase): EncounteredWordDao = db.encounteredWordDao()
    @Provides
    fun provideCatalogedWordDao(db: AppDatabase): CatalogedWordDao = db.catalogedWordDao()
    @Provides
    fun provideWordNoteDao(db: AppDatabase): WordNoteDao = db.wordNoteDao()
    @Provides
    fun provideSeedWordDao(db: AppDatabase): SeedWordDao = db.seedWordDao()

    @Provides
    @Singleton
    fun provideCountersDao(db: AppDatabase): CountersDao = db.countersDao()

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("app_preferences") },
        )

    @Provides
    @Singleton
    fun provideAppPreferences(dataStore: DataStore<Preferences>): AppPreferences =
        AppPreferencesImpl(dataStore)

    @Provides
    @Singleton
    fun provideJson(): Json = Json { ignoreUnknownKeys = true }

    @Provides
    @Singleton
    fun provideRetrofit(json: Json): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.dictionaryapi.dev/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    @Singleton
    fun provideDictionaryApi(retrofit: Retrofit): DictionaryApi = retrofit.create(DictionaryApi::class.java)
}