package com.example.takeaway.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "Hanzi"
)
data class Hanzi(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val character: String,
    val pinyin: String,
    val abbrev: String,
    val tone: Int,
    val strokes: Int,
    val radicals: String,
    @ColumnInfo(name = "old_char")
    val oldCharacter: String,
    val explanation: String?,
    val more: String?
)
