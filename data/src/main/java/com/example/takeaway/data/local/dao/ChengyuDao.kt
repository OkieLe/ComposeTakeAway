package com.example.takeaway.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.takeaway.data.local.entity.Chengyu

@Dao
interface ChengyuDao {

    @Query("SELECT * FROM Chengyu WHERE word = :word")
    suspend fun findByWord(word: String): List<Chengyu>

    @Query("SELECT * FROM Chengyu WHERE ascii_pinyin LIKE :pinyin || '%' OR abbrev = :pinyin")
    suspend fun findByPinyin(pinyin: String): List<Chengyu>

    @Query("SELECT * FROM Chengyu WHERE word LIKE '%' || :hanzi || '%'")
    suspend fun findByHanzi(hanzi: String): List<Chengyu>
}
