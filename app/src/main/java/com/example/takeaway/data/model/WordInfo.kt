package com.example.takeaway.data.model

import androidx.annotation.Keep

@Keep
data class WordInfo(
    val meanings: List<Meaning>,
    val phonetics: List<Phonetic>,
    val word: String
)
