package com.example.takeaway.data.model

import androidx.annotation.Keep

@Keep
data class CiInfo(
    val id: Int = 0,
    val word: String,
    val pinyin: String,
    val explanation: String,
    val example: String,
    val derivation: String
)
