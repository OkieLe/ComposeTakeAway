package com.example.takeaway.data.di

import android.content.Context
import com.example.takeaway.data.local.HanziDataSource
import com.example.takeaway.data.local.HanziDatabase
import com.example.takeaway.data.local.LocalWordsDataSource
import com.example.takeaway.data.local.WordsDatabase
import com.example.takeaway.data.local.dao.ChengyuDao
import com.example.takeaway.data.local.dao.HanziDao
import com.example.takeaway.data.local.dao.WordDao
import com.example.takeaway.data.local.mapper.HanziInfoMapper
import com.example.takeaway.data.local.mapper.WordInfoMapper
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class LocalModule {

    @Singleton
    @Provides
    fun provideLocalWordsDataSource(gson: Gson, wordsDatabase: WordsDatabase): LocalWordsDataSource {
        return LocalWordsDataSource(WordInfoMapper(gson), wordsDatabase.wordDao())
    }

    @Singleton
    @Provides
    fun provideStarredDatabase(@ApplicationContext context: Context): WordsDatabase {
        return WordsDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideWordDao(wordsDatabase: WordsDatabase): WordDao {
        return wordsDatabase.wordDao()
    }

    @Singleton
    @Provides
    fun provideHanziDataSource(hanziDatabase: HanziDatabase): HanziDataSource {
        return HanziDataSource(hanziDatabase.hanziDao(), hanziDatabase.chengyuDao(), HanziInfoMapper())
    }

    @Singleton
    @Provides
    fun provideHanziDatabase(@ApplicationContext context: Context): HanziDatabase {
        return HanziDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideHanziDao(hanziDatabase: HanziDatabase): HanziDao {
        return hanziDatabase.hanziDao()
    }

    @Singleton
    @Provides
    fun provideChengyuDao(hanziDatabase: HanziDatabase): ChengyuDao {
        return hanziDatabase.chengyuDao()
    }
}
