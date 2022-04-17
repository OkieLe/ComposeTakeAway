package com.example.takeaway.data.di

import android.content.Context
import com.example.takeaway.data.local.WordsDatabase
import com.example.takeaway.data.local.dao.WordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideStarredDatabase(@ApplicationContext context: Context): WordsDatabase {
        return WordsDatabase.getInstance(context)
    }

    @Provides
    fun provideWordDao(wordsDatabase: WordsDatabase): WordDao {
        return wordsDatabase.wordDao()
    }
}
