package com.example.takeaway.data

import com.example.takeaway.data.model.DataResult
import com.example.takeaway.data.model.WordInfo
import com.example.takeaway.data.model.wrapDataResult
import com.example.takeaway.data.remote.RemoteDictionaryDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DictionaryRepository @Inject constructor(
    private val remoteDictionaryDataSource: RemoteDictionaryDataSource
) {
    suspend fun searchWord(word: String): DataResult<List<WordInfo>> = wrapDataResult {
        remoteDictionaryDataSource.searchWord(word = word)
    }
}
