package com.example.takeaway.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Chengyu",
    indices = [
        Index(name = "ind_abbr", value = ["abbrev"]),
        Index(name = "ind_pinyin", value = ["ascii_pinyin"])
    ]
)
data class Chengyu(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val word: String,
    val pinyin: String,
    val abbrev: String,
    @ColumnInfo(name = "ascii_pinyin")
    val asciiPinyin: String,
    val explanation: String?,
    val example: String?,
    val derivation: String?
)
