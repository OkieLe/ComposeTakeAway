package com.example.takeaway.data.remote

import com.example.takeaway.data.model.WordInfo
import retrofit2.http.GET
import retrofit2.http.Path

interface RemoteWordsDataSource {
    @GET("entries/en/{word}")
    suspend fun searchWord(@Path("word") word: String): List<WordInfo>
}
