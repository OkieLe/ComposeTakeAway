package com.example.takeaway.data

import com.example.takeaway.data.local.LocalWordsDataSource
import com.example.takeaway.data.model.DataResult
import com.example.takeaway.data.model.WordInfo
import com.example.takeaway.data.model.wrapDataResult
import com.example.takeaway.data.remote.RemoteWordsDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WordsRepository @Inject constructor(
    private val remoteWordsDataSource: RemoteWordsDataSource,
    private val localWordsDataSource: LocalWordsDataSource
) {
    suspend fun searchWord(word: String): DataResult<List<WordInfo>> = wrapDataResult {
        remoteWordsDataSource.searchWord(word = word)
    }

    suspend fun isWordStarred(word: String): Boolean {
        return localWordsDataSource.isWordStarred(word)
    }

    suspend fun starWord(wordInfos: List<WordInfo>) {
        localWordsDataSource.starWord(wordInfos)
    }

    suspend fun unStarWord(word: String) {
        localWordsDataSource.unStarWord(word)
    }

    fun getStarredWords() = localWordsDataSource.starredWords
}
