package com.example.takeaway.data.model

import androidx.annotation.Keep

@Keep
data class Meaning(
    val definitions: List<Definition>,
    val partOfSpeech: String
)
