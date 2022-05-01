package com.example.takeaway.data.model

import androidx.annotation.Keep

@Keep
data class ZiInfo(
    val id: Int = 0,
    val name: String,
    val pinyin: String,
    val strokes: Int,
    val radicals: String,
    val oldName: String,
    val explanation: String,
    val more: String
)
