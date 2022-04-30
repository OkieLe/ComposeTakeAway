package com.example.takeaway.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.takeaway.data.local.entity.Hanzi

@Dao
interface HanziDao {
    @Query("SELECT * FROM Hanzi WHERE abbrev = :pinyin")
    suspend fun findByPinyin(pinyin: String): List<Hanzi>

    @Query("SELECT * FROM Hanzi WHERE character = :hanzi")
    suspend fun findByHanzi(hanzi: String): List<Hanzi>
}
