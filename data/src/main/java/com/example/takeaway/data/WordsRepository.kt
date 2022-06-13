package com.example.takeaway.data

import com.example.takeaway.data.local.LocalWordsDataSource
import com.example.takeaway.data.model.WordInfo
import com.example.takeaway.data.remote.RemoteWordsDataSource

class WordsRepository(
    private val remoteWordsDataSource: RemoteWordsDataSource,
    private val localWordsDataSource: LocalWordsDataSource
) {

    suspend fun searchWord(word: String): List<WordInfo> {
        return remoteWordsDataSource.searchWord(word = word)
    }

    suspend fun isWordStarred(word: String): Boolean {
        return localWordsDataSource.isWordStarred(word)
    }

    suspend fun getWord(word: String): List<WordInfo> {
        return localWordsDataSource.getWord(word)
    }

    suspend fun starWord(wordInfos: List<WordInfo>) {
        localWordsDataSource.starWord(wordInfos)
    }

    suspend fun unStarWord(word: String) {
        localWordsDataSource.unStarWord(word)
    }

    fun getStarredWords() = localWordsDataSource.starredWords
}
