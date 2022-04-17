package com.example.takeaway.data.di

import com.example.takeaway.data.local.LocalWordsDataSource
import com.example.takeaway.data.local.WordsDatabase
import com.example.takeaway.data.local.mapper.WordInfoMapper
import com.example.takeaway.data.remote.RemoteWordsDataSource
import com.example.takeaway.utils.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.DICTIONARY_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Singleton
    @Provides
    fun provideRemoteWordsDataSource(retrofit: Retrofit): RemoteWordsDataSource {
        return retrofit.create(RemoteWordsDataSource::class.java)
    }

    @Singleton
    @Provides
    fun provideLocalWordsDataSource(gson: Gson, wordsDatabase: WordsDatabase): LocalWordsDataSource {
        return LocalWordsDataSource(WordInfoMapper(gson), wordsDatabase.wordDao())
    }
}
