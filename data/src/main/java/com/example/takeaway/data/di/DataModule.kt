package com.example.takeaway.data.di

import com.example.takeaway.data.WordsRepository
import com.example.takeaway.data.local.LocalWordsDataSource
import com.example.takeaway.data.remote.RemoteWordsDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataModule {
    @Singleton
    @Provides
    fun provideWordsRepository(
        remoteWordsDataSource: RemoteWordsDataSource,
        localWordsDataSource: LocalWordsDataSource
    ): WordsRepository {
        return WordsRepository(remoteWordsDataSource, localWordsDataSource)
    }
}
