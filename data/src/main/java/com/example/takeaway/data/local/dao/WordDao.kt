package com.example.takeaway.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.takeaway.data.local.entity.Word
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Query("SELECT DISTINCT word FROM words")
    fun getAllWords(): Flow<List<String>>

    @Query("SELECT * FROM words WHERE word = :word")
    suspend fun findWord(word: String): List<Word>

    @Insert
    suspend fun insert(vararg words: Word): List<Long>

    @Delete
    suspend fun delete(vararg words: Word)
}
