package com.example.takeaway.data.model

import androidx.annotation.Keep

@Keep
data class Definition(
    val definition: String,
    val example: String?,
    val synonyms: List<String>,
    val antonyms: List<String>
)
