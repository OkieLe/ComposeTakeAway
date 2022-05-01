package com.example.takeaway.common.model

data class HanziItem(
    val name: String,
    val pinyin: String,
    val extraInfo: ExtraInfo,
    val explanation: String
)

sealed interface ExtraInfo {
   data class Zi(val strokes: Int, val radicals: String, val oldName: String): ExtraInfo
   data class Ci(val derivation: String): ExtraInfo
}