package com.example.takeaway.data.remote

import com.example.takeaway.data.model.WordInfo
import retrofit2.http.GET
import retrofit2.http.Path

const val DICTIONARY_BASE_URL = "https://api.dictionaryapi.dev/api/v2/"

interface RemoteWordsDataSource {

    @GET("entries/en/{word}")
    suspend fun searchWord(@Path("word") word: String): List<WordInfo>
}
