package com.example.takeaway.word.model

import com.example.takeaway.common.UiState
import com.example.takeaway.common.model.WordItem

data class WordState(
    val wordItems: List<WordItem> = emptyList(),
    val starred: Boolean = true,
): UiState
